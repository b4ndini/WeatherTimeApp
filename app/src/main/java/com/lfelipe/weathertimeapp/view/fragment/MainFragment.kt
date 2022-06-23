package com.lfelipe.weathertimeapp.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.databinding.FragmentMainBinding
import com.lfelipe.weathertimeapp.util.Constants
import com.lfelipe.weathertimeapp.util.GpsLocation
import com.lfelipe.weathertimeapp.view.adapter.MainAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel
import java.util.concurrent.TimeUnit


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest


    companion object {

        private const val PERMISSION_ID = 42
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
           // setLocationRequest()
            setupGpsService()
        }



        binding.apply {
            ivTempIcon.setOnClickListener { navToDetail() }
            tvCity.setOnClickListener { navToDetail() }
            tvTemp.setOnClickListener { navToDetail() }
            tvSuburb.setOnClickListener { navToDetail() }
            ivErrorIcon.setOnClickListener {
               // setLocationRequest()
                setupGpsService()
            }
        }

        setupObserver()

    }

    private fun navToDetail() {
        val city = viewModel.locationLiveData.value?.address?.city ?: ""
        val locationName = viewModel.locationLiveData.value?.displayName ?: ""
        val action =
            MainFragmentDirections.actionMainFragmentToWeatherDetailFragment(GpsLocation.location,
                city, locationName)
        findNavController().navigate(action)
    }


    private fun setupObserver() {
        viewModel.currentLocalWeatherLiveData.observe(viewLifecycleOwner, {
            viewModel.getLocation(GpsLocation.latitude, GpsLocation.longitude)
            viewModel.getWeekForecast(GpsLocation.location)
            setErrorUI(false)
            settingProgressBar(false)
            it.let {
                binding.tvTemp.text = it.current.temperature.toString() + "ºC"
                binding.tvRealFeel.text = "Sensação: " + it.current.feelsLikeTemp.toString() + "ºC"
                binding.tvPrecipRate.text = it.current.precipProb.toString() + "%"
                binding.tvWindSpeed.text = it.current.windSpeed.toString() + " km/h"
                val image = Constants.FORECA_IMAGE_URL + it.current.symbol + ".png"
                Glide.with(this).load(image).into(binding.ivTempIcon)
                var text = it.current.symbolPhrase
                text = text.replaceFirstChar { first ->
                    first.uppercaseChar()
                }

                binding.tvDescription.text = text

                setWeatherBackground(it.current.symbol)

            }

        })

        viewModel.currentWeekForecastLiveData.observe(viewLifecycleOwner, {
            it.let {
                binding.rvForecast.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = MainAdapter(it.forecast)
                }
            }
        })

        viewModel.errorMsgLiveData.observe(viewLifecycleOwner, {
            setErrorUI(true, getString(R.string.api_connection_fail))
            settingProgressBar(false)
            binding.ivErrorIcon.setOnClickListener {
                viewModel.getToken()
                setErrorUI(false)
                settingProgressBar(true, getString(R.string.loading_data_msg))
            }

        })


        viewModel.token.observe(viewLifecycleOwner, {

            viewModel.getCurrentLocalWeather(GpsLocation.location)


        })

        viewModel.locationLiveData.observe(viewLifecycleOwner, {
            it.let { location ->

                if(!location.address.city.isNullOrBlank() && !location.address.suburb.isNullOrBlank()){
                    binding.tvCity.text = location.address.city
                    binding.tvSuburb.text = location.address.suburb
                }else{
                    val locationDataList = location.displayName.split(",")
                    binding.tvSuburb.text = locationDataList[0]
                    binding.tvCity.text = locationDataList[1]
                }
            }
        })
    }

    private fun setWeatherBackground(condition: String) {
        when {
            condition.startsWith("n") -> {
                binding.activity.setBackgroundResource(R.drawable.night_shape)
            }
            condition[2] == '1' -> {
                binding.activity.setBackgroundResource(R.drawable.rainy_shape)
            }
            else -> {
                binding.activity.setBackgroundResource(R.drawable.sunny_shape)
            }
        }
    }

    private fun setupGpsService() {
        if (checkPermissions()) {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            getCurrentLocationGPS()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
            )
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("TAG", "onRequestPermissionResult")

        when (requestCode) {
            PERMISSION_ID -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    return

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission was granted.
                    fusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(requireActivity())
                    getCurrentLocationGPS()
                }

                else -> {
                    // Permission denied.
                    setErrorUI(true, getString(R.string.grant_permissions))

                }
            }
        }
    }


    private fun getCurrentLocationGPS() {
        if (isLocationEnabled()) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { location ->
                    val myLocation: Location? = location.result
                    if (myLocation == null) {
                        requestNewLocation()
                    } else {
                        GpsLocation.location = "${myLocation.longitude},${myLocation.latitude}"
                        GpsLocation.latitude = myLocation.latitude.toString()
                        GpsLocation.longitude = myLocation.longitude.toString()
                        setErrorUI(false)
                        settingProgressBar(true, getString(R.string.loading_data_msg))
                        viewModel.getToken()
                    }
                }
            }
        } else {
            enableGps()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun setLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            numUpdates = 1
            expirationTime = 6000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocation() {

        setLocationRequest()

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallBack,
            Looper.getMainLooper())
    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            getCurrentLocationGPS()
           /* val lastLocation = locationResult.lastLocation
            GpsLocation.location = "${lastLocation.longitude},${lastLocation.latitude}"
            GpsLocation.latitude = lastLocation.latitude.toString()
            GpsLocation.longitude = lastLocation.longitude.toString()
            setErrorUI(false)
            settingProgressBar(true, getString(R.string.loading_data_msg))
            viewModel.getToken()*/
        }
    }

    private fun enableGps() {

        setLocationRequest()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {

                        val resolvableApiExc = ex as ResolvableApiException
                        startIntentSenderForResult(resolvableApiExc.resolution.intentSender,
                            PERMISSION_ID,
                            null,
                            0,
                            0,
                            0,
                            null)
                    } catch (ex: IntentSender.SendIntentException) {
                        Toast.makeText(context,
                            "PendingIntent unable to execute request.",
                            Toast.LENGTH_SHORT).show()
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Toast.makeText(
                            context,
                            "Something is wrong in your GPS",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_ID) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    settingProgressBar(true)
                    getCurrentLocationGPS()
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    Log.d("TAG", "User denied to access location")
                    setErrorUI(true, getString(R.string.enable_gps))

                }
            }
        } else {
            enableGps()
        }
    }


    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    private fun setErrorUI(set: Boolean, msg: String? = null) {
        if (set) {
            binding.clickableContainer.visibility = GONE
            binding.errorContainer.visibility = VISIBLE
            binding.tvErrorMsg.text = msg
        } else {
            binding.clickableContainer.visibility = VISIBLE
            binding.errorContainer.visibility = GONE
        }
    }


    private fun settingProgressBar(state: Boolean, msg: String? = null) {
        if (state) {
            binding.containerLoading.visibility = VISIBLE
            binding.tvLoadingMsg.text = msg ?: getString(R.string.loading_location_msg)

        } else {
            binding.containerLoading.visibility = GONE

        }
    }

}