package com.lfelipe.weathertimeapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.lfelipe.weathertimeapp.databinding.FragmentWeekForecastBinding
import com.lfelipe.weathertimeapp.view.adapter.MainAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel



class WeekForecastFragment : Fragment() {

    private lateinit var binding: FragmentWeekForecastBinding
    private val args: WeekForecastFragmentArgs by navArgs()
    private lateinit var viewModel: MainViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWeekForecastBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.getWeekForecast(args.location)
        }

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.currentWeekForecastLiveData.observe(viewLifecycleOwner){
            it?.let {  week ->
                binding.rvWeekForecast.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = MainAdapter(week.forecast)
                }
            }
        }
    }

}