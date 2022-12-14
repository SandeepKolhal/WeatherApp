package com.android.weatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.weatherapp.R
import com.android.weatherapp.databinding.FragmentSettingBinding
import com.android.weatherapp.utils.Constants.CELSIUS
import com.android.weatherapp.utils.Constants.FAHRENHEIT
import com.android.weatherapp.utils.Constants.KMH
import com.android.weatherapp.utils.Constants.MPH
import com.android.weatherapp.viewmodels.MainViewModel

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var selectedTempUnit: String? = null
    private var selectedWindUnit: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        binding.apply {

            back.setOnClickListener {
                findNavController().navigateUp()
            }

            celsius.setOnClickListener {
                if (selectedTempUnit != null && selectedTempUnit != CELSIUS) {
                    viewModel.setSelectedTempUnit(CELSIUS)
                }
            }

            fahrenheit.setOnClickListener {
                if (selectedTempUnit == null || selectedTempUnit != FAHRENHEIT) {
                    viewModel.setSelectedTempUnit(FAHRENHEIT)
                }
            }

            kilometer.setOnClickListener {
                if (selectedWindUnit != null && selectedWindUnit != KMH) {
                    viewModel.setSelectedWindUnit(KMH)
                }
            }

            miles.setOnClickListener {
                if (selectedWindUnit == null || selectedWindUnit != MPH) {
                    viewModel.setSelectedWindUnit(MPH)
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            tempUnit.observe(viewLifecycleOwner) {
                selectedTempUnit = it
                updateTempUnitUI(it)
            }
            windUnit.observe(viewLifecycleOwner) {
                selectedWindUnit = it
                updateWindUnitUI(it)
            }
        }
    }

    private fun updateWindUnitUI(unit: String?) {
        when (unit) {
            KMH, null -> {
                binding.kilometer.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.black
                    )
                )
                binding.miles.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.light_grey_DADADA
                    )
                )
            }
            MPH -> {
                binding.kilometer.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.light_grey_DADADA
                    )
                )
                binding.miles.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.black
                    )
                )
            }
        }
    }

    private fun updateTempUnitUI(tempUnit: String?) {
        when (tempUnit) {
            CELSIUS, null -> {
                binding.celsius.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.black
                    )
                )
                binding.fahrenheit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.light_grey_DADADA
                    )
                )
            }
            FAHRENHEIT -> {
                binding.celsius.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.light_grey_DADADA
                    )
                )
                binding.fahrenheit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.black
                    )
                )
            }
        }
    }

}