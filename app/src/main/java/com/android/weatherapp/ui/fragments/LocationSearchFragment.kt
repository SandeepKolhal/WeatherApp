package com.android.weatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.weatherapp.adapters.LocationListAdapter
import com.android.weatherapp.databinding.FragmentLocationSearchBinding
import com.android.weatherapp.viewmodels.MainViewModel

class LocationSearchFragment : Fragment() {

    private lateinit var binding: FragmentLocationSearchBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val locationListAdapter: LocationListAdapter by lazy {
        LocationListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            locationList.adapter = locationListAdapter

            locationListAdapter.setList(
                viewModel.getLocationList()
            )

            locationListAdapter.setOnClickListener(object : LocationListAdapter.OnClickListener {
                override fun onItemClick(location: String) {
                    viewModel.setSelectedLocation(location)
                    findNavController().navigateUp()
                }
            })

            back.setOnClickListener {
                findNavController().navigateUp()
            }
            searchLocation.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.setSelectedLocation(it) }
                    findNavController().navigateUp()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }
}