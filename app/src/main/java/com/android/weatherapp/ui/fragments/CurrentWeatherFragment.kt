package com.android.weatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.android.weatherapp.models.result.ResultData
import com.android.weatherapp.viewmodels.MainViewModel
import kotlinx.coroutines.flow.catch


class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.resultData.catch {
                it.printStackTrace()
            }.collect { resultData ->
                when (resultData) {
                    is ResultData.Failure -> {

                    }
                    is ResultData.Loading -> {

                    }
                    is ResultData.Success -> {

                    }
                }
            }
        }


        viewModel.fetchForecastData(getRequestData())
    }

    private fun getRequestData(): Map<String, String> {
        val param = mutableMapOf<String, String>()
        param["q"] = "mumbai"
        param["days"] = "7"
        return param
    }
}