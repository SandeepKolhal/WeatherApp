package com.android.weatherapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ActivityMainBinding
import com.android.weatherapp.datasources.RemoteApiDataSourceImpl
import com.android.weatherapp.datasources.UserPreferenceLocalDataStoreImpl
import com.android.weatherapp.datasources.WeatherForecastDataSourceImpl
import com.android.weatherapp.network.RemoteApiClient
import com.android.weatherapp.repositories.UserPreferenceRepositoryImpl
import com.android.weatherapp.repositories.WeatherForecastRepositoryImpl
import com.android.weatherapp.viewmodels.MainViewModel
import com.android.weatherapp.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        initViewModel()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(
                MainViewModel(
                    WeatherForecastRepositoryImpl(
                        WeatherForecastDataSourceImpl(
                            RemoteApiDataSourceImpl(RemoteApiClient.getRemoteApiService())
                        )
                    ),
                    UserPreferenceRepositoryImpl(UserPreferenceLocalDataStoreImpl(applicationContext))
                )
            )
        )[MainViewModel::class.java]
    }
}