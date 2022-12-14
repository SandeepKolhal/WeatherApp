package com.android.weatherapp.ui.fragments

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.weatherapp.R
import com.android.weatherapp.adapters.ForecastListAdapter
import com.android.weatherapp.adapters.HourlyListAdapter
import com.android.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.forecast.Forecastday
import com.android.weatherapp.utils.Constants.CELSIUS
import com.android.weatherapp.utils.Constants.FAHRENHEIT
import com.android.weatherapp.utils.Constants.KMH
import com.android.weatherapp.utils.Constants.MPH
import com.android.weatherapp.utils.setConditionImage
import com.android.weatherapp.viewmodels.MainViewModel
import java.net.UnknownHostException


class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding

    private val viewModel: MainViewModel by activityViewModels()
    private val hourlyListAdapter by lazy {
        HourlyListAdapter()
    }

    private val forecastListAdapter by lazy {
        ForecastListAdapter()
    }

    private val lastSearchParam = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        initViewModelObservers()

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

            menu.setOnClickListener {
                showPopup(it)
            }

            swipeToRefresh.setOnRefreshListener { viewModel.fetchForecastData(getRequestData()) }
        }

        forecastListAdapter.setOnClickListener(object : ForecastListAdapter.OnClickListener {
            override fun onItemClick(forecastDay: Forecastday) {
                viewModel.setSelectedForecastDay(forecastDay)
                findNavController().navigate(R.id.action_currentWeatherFragment_to_forecastWeatherDetailFragment)
            }
        })
    }

    private fun initViewModelObservers() {
        viewModel.apply {
            weatherForecastDetails.observe(viewLifecycleOwner) {
                it?.let { forecastResponsne ->
                    updateUI(forecastResponsne)
                } ?: run {
                    updateErrorUI(getString(R.string.network_error))
                }

            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeToRefresh.isRefreshing = false
                }
            }

            forecastApiError.observe(viewLifecycleOwner) { errorData ->
                binding.progressBar.visibility = View.GONE
                var errorMessage = ""
                errorData.throwable?.let {
                    errorMessage = when (it) {
                        is NetworkErrorException, is UnknownHostException -> {
                            getString(R.string.network_error)
                        }
                        else -> {
                            it.localizedMessage ?: getString(R.string.network_error)
                        }
                    }
                } ?: run {
                    errorData.error?.let {
                        errorMessage = "${it.error.message} ${it.error.code}"
                    } ?: run {
                        errorMessage =
                            errorData.apiErrorMessage ?: getString(R.string.network_error)
                    }
                }

                updateErrorUI(errorMessage)
            }

            tempUnit.observe(viewLifecycleOwner) {
                it?.let { unit ->
                    val forecastDetails = viewModel.weatherForecastDetails.value
                    forecastDetails?.let {
                        updateTemperatureDetails(forecastDetails, unit)
                    }
                }
            }

            windUnit.observe(viewLifecycleOwner) {
                it?.let { unit ->
                    val forecastDetails = viewModel.weatherForecastDetails.value
                    forecastDetails?.let {
                        updateWindDetails(forecastDetails, unit)
                    }
                }
            }

            lastLocationSearched.observe(viewLifecycleOwner) {
                fetchForecastData(getRequestData(it))
            }

            fetchAllUserPreferenceData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateWindDetails(forecastDetails: ForecastResponse, unit: String) {
        binding.apply {
            var windText = ""
            when (unit) {
                KMH -> {
                    windText = "${forecastDetails.current.windKph} ${getString(R.string.km_h)}"
                }
                MPH -> {
                    windText = "${forecastDetails.current.windMph} ${getString(R.string.mph)}"
                }
            }
            windValue.text = windText
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperatureDetails(forecastDetails: ForecastResponse, unit: String) {
        binding.apply {
            val degreeSymbol = getString(R.string.degree_symbol)
            var currentTemp = ""
            var maxTemp = ""
            var minTemp = ""
            var feelLikeTemp = ""
            var unitText = ""
            when (unit) {
                CELSIUS -> {
                    currentTemp = "${forecastDetails.current.tempC}"
                    maxTemp = "${forecastDetails.forecast.forecastDay[0].day.maxTempC}$degreeSymbol"
                    minTemp = "${forecastDetails.forecast.forecastDay[0].day.minTempC}$degreeSymbol"
                    feelLikeTemp = "${forecastDetails.current.feelsLikeC}$degreeSymbol"
                    unitText = getString(R.string.celcius_unit)
                }
                FAHRENHEIT -> {
                    currentTemp = "${forecastDetails.current.tempF}"
                    maxTemp = "${forecastDetails.forecast.forecastDay[0].day.maxTempF}$degreeSymbol"
                    minTemp = "${forecastDetails.forecast.forecastDay[0].day.minTempF}$degreeSymbol"
                    feelLikeTemp = "${forecastDetails.current.feelsLikeF}$degreeSymbol"
                    unitText = getString(R.string.fahrenheit_unit)
                }
            }
            tempValue.text = currentTemp
            tempUnit.text = unitText
            currentWeatherInfo.text =
                "$maxTemp / $minTemp ${getString(R.string.feels_like)} $feelLikeTemp"

            hourlyListAdapter.setList(forecastDetails.forecast.forecastDay[0].hour, unit)
            forecastListAdapter.setList(getForecastList(forecastDetails.forecast.forecastDay), unit)
        }
    }

    private fun updateErrorUI(errorMessage: String) {
        binding.weatherDetailView.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.errorMessage.text = errorMessage
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(forecastResponse: ForecastResponse) {

        binding.apply {
            weatherDetailView.visibility = View.VISIBLE
            errorView.visibility = View.GONE

            currentConditionIcon.setConditionImage(forecastResponse.current.condition.icon)
            currentWeatherCondition.text = forecastResponse.current.condition.text
            locationText.text = forecastResponse.location.name
            sunriseValue.text = forecastResponse.forecast.forecastDay[0].astro.sunrise
            sunrsetValue.text = forecastResponse.forecast.forecastDay[0].astro.sunset
            humidityValue.text =
                "${forecastResponse.current.humidity}${getString(R.string.percent_symbol)}"

            pressureValue.text = "${forecastResponse.current.pressureMb} ${getString(R.string.mb)}"

            val tempUnit = viewModel.tempUnit.value
            val windUnit = viewModel.windUnit.value

            updateTemperatureDetails(forecastResponse, tempUnit ?: CELSIUS)
            updateWindDetails(forecastResponse, windUnit ?: MPH)
        }
    }

    //This is to remove current day from forecast list
    private fun getForecastList(forecastDay: List<Forecastday>): List<Forecastday> {
        val forecastList = mutableListOf<Forecastday>()
        forecastList.addAll(forecastDay)
        forecastList.removeFirst()
        return forecastList
    }

    private fun getRequestData(lastSearchedData: String? = null): Map<String, String> {
        if (lastSearchParam.isEmpty()) {
            lastSearchParam["q"] = lastSearchedData ?: "auto:ip"
            lastSearchParam["days"] = "7"
        } else {
            lastSearchedData?.let {
                lastSearchParam["q"] = it
            }
        }
        return lastSearchParam
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireActivity(), view)
        popup.inflate(R.menu.home_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.setting -> {
                    findNavController().navigate(R.id.action_currentWeatherFragment_to_settingFragment)
                }
                R.id.location -> {
                    findNavController().navigate(R.id.action_currentWeatherFragment_to_locationSearchFragment)
                }
            }
            true
        }

        popup.show()
    }
}