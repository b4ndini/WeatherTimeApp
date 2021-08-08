package com.lfelipe.weathertimeapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lfelipe.weathertimeapp.databinding.FragmentWeatherDetailBinding
import com.lfelipe.weathertimeapp.util.Constants
import com.lfelipe.weathertimeapp.view.adapter.HourlyAdapter
import com.lfelipe.weathertimeapp.view.adapter.MainAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel


class WeatherDetailFragment : Fragment() {

    private val args: WeatherDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentWeatherDetailBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWeatherDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            val loc = args.location
            viewModel.getWeekForecast(loc)
            viewModel.getHourlyForecast(loc)
            viewModel.getCurrentLocalWeather(loc)
        }

        setupObserve()

    }

    private fun setupObserve() {
        //TODO viewModel errors livedata
        viewModel.currentWeekForecastLiveData.observe(viewLifecycleOwner, {
            it?.let{ week ->
                binding.rvWeekForecast.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = MainAdapter(week.forecast)
                }
            }
        })

        viewModel.hourlyLiveData.observe(viewLifecycleOwner, {
            it?.let{ hourly ->
                binding.rvHourlyForecast.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = HourlyAdapter(hourly.forecast)
                }

            }
        })

        viewModel.currentLocalWeatherLiveData.observe(viewLifecycleOwner, {
            it?.let{
                binding.apply {
                    val cw = it.current
                    val image = Constants.FORECA_IMAGE_URL + it.current.symbol + ".png"
                    Glide.with(this@WeatherDetailFragment).load(image).into(binding.ivWeatherIcon)
                    tvDescription.text = cw.symbolPhrase
                    tvTemp.text = cw.temperature.toString()+"º"
                    tvRealFeel.text = "Sensação:\n ${cw.feelsLikeTemp}º"
                    tvDewPoint.text = "Ponto de orvalho:\n ${cw.dewPoint}º"
                    tvWindSpeed.text = "Vento:\n ${cw.windSpeed} km/h"
                    tvPrecipProb.text = "Prob. Chuva:\n ${cw.precipProb}%"
                    tvPrecipRate.text = "Volume:\n ${cw.precipRate} mm"
                    tvThunderProb.text = "Prob. Raio:\n ${cw.thunderProb}%"
                    tvHumidity.text = "Umidade:\n ${cw.relHumidity}%"
                    tvVisibility.text = "Visib:\n ${cw.visibility/1000} km"
                    tvIndexUV.text = "Índice UV:\n ${cw.uvIndex}"
                    tvCity.text = args.city

                }

            }
        })
    }

}