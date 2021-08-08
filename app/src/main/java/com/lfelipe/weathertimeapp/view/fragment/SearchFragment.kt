package com.lfelipe.weathertimeapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lfelipe.weathertimeapp.databinding.FragmentSearchBinding
import com.lfelipe.weathertimeapp.view.adapter.SearchAdapter
import com.lfelipe.weathertimeapp.viewmodel.MainViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    

    override fun onResume() {
        super.onResume()
        binding.svSearch.requestFocus()
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    override fun onPause(){
        super.onPause()
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            binding.svSearch.windowToken, 0
        )

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        }

        setupSearch(binding.svSearch)
        setupObserver()

    }

    private fun setupObserver() {
        viewModel.searchLiveData.observe(viewLifecycleOwner, {
            it?.let { location ->
                binding.rvLocations.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter = SearchAdapter(location.locations){ position ->
                            val local = location.locations[position].id.toString()
                            val city = location.locations[position].name
                            val action = SearchFragmentDirections.actionSearchFragmentToWeatherDetailFragment(local, city)
                            findNavController().navigate(action)

                        }
                }
            }
        })
    }

    private fun setupSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return true }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.setSearch(query ?: "")

                return true
            }
        })
    }

}