package com.lfelipe.weathertimeapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
          //  viewModel.getToken()
        }

        setupSearch(binding.svSearch)
        setupObserver()

    }

    private fun setupObserver() {
        viewModel.searchLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.rvLocations.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter = SearchAdapter(it.locations)
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