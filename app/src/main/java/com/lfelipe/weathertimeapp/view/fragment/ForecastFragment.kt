package com.lfelipe.weathertimeapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lfelipe.weathertimeapp.databinding.FragmentForecastBinding
import com.lfelipe.weathertimeapp.util.GpsLocation
import com.lfelipe.weathertimeapp.view.adapter.DailyAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel

class ForecastFragment : Fragment() {


    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.getDailyForecast(GpsLocation.location)
        }

        setupObserver()


    }

    private fun setupObserver() {

        viewModel.dailyForecastLiveData.observe(viewLifecycleOwner,{
            it.let{
                binding.rvDailyForecast.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = DailyAdapter(it.forecast)
                }
            }
        })

        viewModel.errorDailyLiveData.observe(viewLifecycleOwner, {
            viewModel.getToken()
        })

        viewModel.token.observe(viewLifecycleOwner,{
            viewModel.getDailyForecast(GpsLocation.location)
        })

    }


}