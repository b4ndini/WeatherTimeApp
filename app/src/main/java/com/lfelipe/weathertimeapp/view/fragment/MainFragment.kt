package com.lfelipe.weathertimeapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.databinding.FragmentMainBinding
import com.lfelipe.weathertimeapp.util.Constants
import com.lfelipe.weathertimeapp.util.GpsLocation
import com.lfelipe.weathertimeapp.view.adapter.MainAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

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
            viewModel.getToken()
        }
        setupObserver()

    }


    private fun setupObserver() {
        viewModel.currentLocalWeatherLiveData.observe(viewLifecycleOwner,{
            binding.errorContainer.visibility = View.INVISIBLE
            it.let {
                binding.tvTemp.text = it.current.temperature.toString() +"ºC"
                binding.tvRealFeel.text = "Sensação: "+ it.current.feelsLikeTemp.toString() + "ºC"
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

        viewModel.currentWeekForecastLiveData.observe(viewLifecycleOwner,{
            it.let{
                binding.rvForecast.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = MainAdapter(it.forecast)
                }
            }
        })

        viewModel.errorMsgLiveData.observe(viewLifecycleOwner,{
            binding.errorContainer.visibility = View.VISIBLE
            binding.ivErrorIcon.setOnClickListener {
                viewModel.getToken()
                Toast.makeText(context, GpsLocation.longitude, Toast.LENGTH_LONG).show()
            }

        })


        viewModel.token.observe(viewLifecycleOwner,{

            viewModel.getCurrentLocalWeather(GpsLocation.location)
            viewModel.getWeekForecast(GpsLocation.location)
            viewModel.getLocation(GpsLocation.latitude, GpsLocation.longitude)
        })

        viewModel.locationLiveData.observe(viewLifecycleOwner,{
            it.let{ location ->
                binding.tvCity.text = location.address.city
                binding.tvSuburb.text = location.address.suburb

            }
        })
    }

    private fun getToken() {

        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.getToken()
        }

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

}