package com.android.weatherapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.weatherapp.databinding.ActivityMainBinding
import com.android.weatherapp.datasources.RemoteApiDataSourceImpl
import com.android.weatherapp.datasources.WeatherForecastDataSourceImpl
import com.android.weatherapp.network.RemoteApiClient
import com.android.weatherapp.repositories.WeatherForecastRepositoryImpl
import com.android.weatherapp.viewmodels.MainViewModel
import com.android.weatherapp.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                MainViewModel(
                    WeatherForecastRepositoryImpl(
                        WeatherForecastDataSourceImpl(
                            RemoteApiDataSourceImpl(RemoteApiClient.getRemoteApiService())
                        )
                    )
                )
            )
        )[MainViewModel::class.java]
    }
}