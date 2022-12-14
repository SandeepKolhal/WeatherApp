package com.android.weatherapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.weatherapp.R
import com.android.weatherapp.adapters.HourlyListAdapter
import com.android.weatherapp.databinding.FragmentForecastWeatherDetailBinding
import com.android.weatherapp.models.forecast.Forecastday
import com.android.weatherapp.utils.Constants.CELSIUS
import com.android.weatherapp.utils.Constants.FAHRENHEIT
import com.android.weatherapp.utils.Constants.KMH
import com.android.weatherapp.utils.Constants.MPH
import com.android.weatherapp.utils.setConditionImage
import com.android.weatherapp.viewmodels.MainViewModel

class ForecastWeatherDetailFragment : Fragment() {

    private lateinit var binding: FragmentForecastWeatherDetailBinding
    private val viewModel: MainViewModel by activityViewModels()

    private val hourlyListAdapter by lazy {
        HourlyListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForecastWeatherDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            back.setOnClickListener {
                findNavController().navigateUp()
            }
            hourlyList.adapter = hourlyListAdapter
        }

        initViewModelObservers()
    }

    private fun initViewModelObservers() {
        viewModel.forecastDay.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI(forecastDay: Forecastday) {
        val degreeSymbol = getString(R.string.degree_symbol)
        binding.apply {
            when (viewModel.tempUnit.value) {
                CELSIUS, null -> {
                    tempValue.text = "${forecastDay.day.avgTempC}"
                    tempUnit.text = getString(R.string.celcius_unit)
                    forecastWeatherInfo.text =
                        "${forecastDay.day.maxTempC}$degreeSymbol " + "/ ${forecastDay.day.minTempC}$degreeSymbol"
                }
                FAHRENHEIT -> {
                    tempValue.text = "${forecastDay.day.avgTempF}"
                    tempUnit.text = getString(R.string.fahrenheit_unit)
                    forecastWeatherInfo.text =
                        "${forecastDay.day.maxTempF}$degreeSymbol " + "/ ${forecastDay.day.minTempF}$degreeSymbol"
                }
            }

            forecastConditionIcon.setConditionImage(forecastDay.day.condition.icon)
            forecastWeatherCondition.text = forecastDay.day.condition.text

            sunriseValue.text = forecastDay.astro.sunrise
            sunrsetValue.text = forecastDay.astro.sunset
            humidityValue.text =
                "${forecastDay.day.avgHumidity}${getString(R.string.percent_symbol)}"

            when (viewModel.windUnit.value) {
                KMH, null -> {
                    windValue.text = "${forecastDay.day.avgvisKm} ${getString(R.string.km_h)}"
                }
                MPH -> {
                    windValue.text = "${forecastDay.day.avgvisMiles} ${getString(R.string.mph)}"
                }
            }

            dateInfo.text = forecastDay.date

            hourlyListAdapter.setList(forecastDay.hour, viewModel.tempUnit.value ?: CELSIUS)

        }
    }

}