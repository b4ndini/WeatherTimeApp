package com.lfelipe.weathertimeapp.view.activity


import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lfelipe.weathertimeapp.util.Constants.FORECA_IMAGE_URL
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.databinding.ActivityMainBinding
import com.lfelipe.weathertimeapp.view.adapter.MainAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getToken()
        setupObserver()
    }


    private fun setupObserver() {
        viewModel.currentLocalWeatherLiveData.observe(this,{
            binding.errorContainer.visibility = INVISIBLE
            it.let {
                binding.tvTemp.text = it.current.temperature.toString() +"ºC"
                binding.tvRealFeel.text = "Sensação: "+ it.current.feelsLikeTemp.toString() + "ºC"
                binding.tvPrecipRate.text = it.current.precipRate.toString() + "%"
                binding.tvWindSpeed.text = it.current.windSpeed.toString() + " km/h"
                val image = FORECA_IMAGE_URL + it.current.symbol + ".png"
                Glide.with(this).load(image).into(binding.ivTempIcon)
                var text = it.current.symbolPhrase
                text = text.replaceFirstChar { first ->
                   first.uppercaseChar()
                }

                binding.tvDescription.text = text

                setWeatherBackground(it.current.symbol)

            }

        })

        viewModel.currentWeekForecastLiveData.observe(this,{
            it.let{
                binding.rvForecast.apply{
                    layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    adapter = MainAdapter(it.forecast)
                }
            }
        })

        viewModel.errorMsgLiveData.observe(this,{
            binding.errorContainer.visibility = VISIBLE
            binding.ivErrorIcon.setOnClickListener {
                viewModel.getToken()
            }

        })


        viewModel.token.observe(this,{

            viewModel.getCurrentLocalWeather("-51.17380,-30.0158")
            viewModel.getWeekForecast("-51.17380,-30.0158")
            viewModel.getLocation("-30.0158", "-51.17380")
        })

        viewModel.locationLiveData.observe(this,{
            it.let{ location ->
                binding.tvCity.text = location.address.city
                binding.tvSuburb.text = location.address.suburb

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


}