package com.android.weatherapp.ui.fragments

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.weatherapp.R
import com.android.weatherapp.adapters.ForecastListAdapter
import com.android.weatherapp.adapters.HourlyListAdapter
import com.android.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.forecast.Forecastday
import com.android.weatherapp.utils.setConditionImage
import com.android.weatherapp.viewmodels.MainViewModel


class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding

    private val viewModel: MainViewModel by activityViewModels()
    private val hourlyListAdapter by lazy {
        HourlyListAdapter()
    }

    private val forecastListAdapter by lazy {
        ForecastListAdapter()
    }

    private lateinit var lastSearchParam: Map<String, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            currentHourlyList.adapter = hourlyListAdapter
            forecastList.adapter = forecastListAdapter
            retry.setOnClickListener {
                viewModel.fetchForecastData(getRequestData())
            }
        }

        initViewModelObservers()

        viewModel.fetchForecastData(getRequestData())
    }

    private fun initViewModelObservers() {
        viewModel.weatherForecastDetails.observe(requireActivity()) {
            it?.let { forecastResposne ->
                updateUI(forecastResposne)
            } ?: run {
                updateErrorUI(getString(R.string.network_error))
            }

        }

        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.forecastApiError.observe(requireActivity()) { errorData ->
            var errorMessage = ""
            errorData.throwable?.let {
                when (it) {
                    is NetworkErrorException -> {
                        errorMessage = getString(R.string.network_error)
                    }
                    else -> {
                        errorMessage = it.localizedMessage ?: getString(R.string.network_error)
                    }
                }
            } ?: run {
                errorData.error?.let {
                    errorMessage = "${it.error.message} ${it.error.code}"
                } ?: run {
                    errorMessage = errorData.apiErrorMessage ?: getString(R.string.network_error)
                }
            }

            updateErrorUI(errorMessage)
        }
    }

    private fun updateErrorUI(errorMessage: String) {
        binding.weatherDetailView.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.errorMessage.text = errorMessage
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(forecastResponse: ForecastResponse) {
        val degreeSymbol = getString(R.string.degree_symbol)
        binding.apply {
            weatherDetailView.visibility = View.VISIBLE
            errorView.visibility = View.GONE

            tempValue.text = "${forecastResponse.current.tempC}"// set cal
            currentWeatherInfo.text =
                "${forecastResponse.forecast.forecastDay[0].day.maxTempC}$degreeSymbol " + "/ ${forecastResponse.forecast.forecastDay[0].day.maxTempC}$degreeSymbol " + "${
                    getString(
                        R.string.feels_like
                    )
                } " + "${forecastResponse.current.feelsLikeC}$degreeSymbol"

            currentConditionIcon.setConditionImage(forecastResponse.current.condition.icon)
            currentWeatherCondition.text = forecastResponse.current.condition.text
            locationText.text = forecastResponse.location.name
            sunriseValue.text = forecastResponse.forecast.forecastDay[0].astro.sunrise
            sunrsetValue.text = forecastResponse.forecast.forecastDay[0].astro.sunset
            humidityValue.text =
                "${forecastResponse.current.humidity}${getString(R.string.percent_symbol)}"


            windValue.text = "${forecastResponse.current.windKph} ${getString(R.string.km_h)}"

            pressureValue.text = "${forecastResponse.current.pressureMb} ${getString(R.string.mb)}"

            hourlyListAdapter.setList(forecastResponse.forecast.forecastDay[0].hour)

            forecastListAdapter.setList(getForecastList(forecastResponse.forecast.forecastDay))
        }
    }

    //This is to remove current day from forecast list
    private fun getForecastList(forecastDay: List<Forecastday>): List<Forecastday> {
        val forecastList = mutableListOf<Forecastday>()
        forecastList.addAll(forecastDay)
        forecastList.removeFirst()
        return forecastList
    }

    private fun getRequestData(): Map<String, String> {
        val param = mutableMapOf<String, String>()
        param["q"] = "auto:ip"
        param["days"] = "7"
        lastSearchParam = param
        return lastSearchParam
    }
}