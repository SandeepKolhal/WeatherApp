package com.android.weatherapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.databinding.HourlyWeatherItemBinding
import com.android.weatherapp.models.forecast.Hour
import com.android.weatherapp.utils.Constants.CELSIUS
import com.android.weatherapp.utils.Constants.FAHRENHEIT
import com.android.weatherapp.utils.getTime
import com.android.weatherapp.utils.setConditionImage

class HourlyListAdapter : RecyclerView.Adapter<HourlyListAdapter.HourlyListViewHolder>() {

    private var hourlyDataList: List<Hour>? = null

    private var tempUnit: String = ""

    fun setList(list: List<Hour>, tempUnit: String) {
        this.tempUnit = tempUnit
        hourlyDataList = list
        notifyItemRangeChanged(0, list.size)
    }

    inner class HourlyListViewHolder(private val binding: HourlyWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        @SuppressLint("SetTextI18n")
        fun bind(item: Hour) {
            binding.apply {
                timeText.text = item.timeEpoch.getTime()

                var tempText = ""
                when (tempUnit) {
                    CELSIUS -> {
                        tempText = "${item.tempC} ${context.getString(R.string.degree_symbol)}"
                    }
                    FAHRENHEIT -> {
                        tempText = "${item.tempF} ${context.getString(R.string.degree_symbol)}"
                    }
                }

                tempValue.text = tempText

                hourlyConditionIcon.setConditionImage(item.condition.icon)
                hourlyHumidity.text =
                    "${item.humidity} ${context.getString(R.string.percent_symbol)}"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HourlyListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HourlyListViewHolder(
            HourlyWeatherItemBinding.inflate(
                inflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyListAdapter.HourlyListViewHolder, position: Int) {
        holder.bind(hourlyDataList!![position])
    }

    override fun getItemCount(): Int {
        return hourlyDataList?.size ?: 0
    }
}
