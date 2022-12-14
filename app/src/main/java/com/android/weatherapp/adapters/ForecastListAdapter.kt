package com.android.weatherapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ForecastListItemBinding
import com.android.weatherapp.models.forecast.Forecastday
import com.android.weatherapp.utils.Constants.CELSIUS
import com.android.weatherapp.utils.Constants.FAHRENHEIT
import com.android.weatherapp.utils.setConditionImage
import com.android.weatherapp.utils.toDay

class ForecastListAdapter : RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder>() {

    private var forecastListData: List<Forecastday>? = null
    private var onClickListener: OnClickListener? = null

    private var tempUnit: String = ""

    fun setList(list: List<Forecastday>, tempUnit: String) {
        this.tempUnit = tempUnit
        forecastListData = list
        notifyItemRangeChanged(0, list.size)
    }

    inner class ForecastListViewHolder(private val binding: ForecastListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        @SuppressLint("SetTextI18n")
        fun bind(item: Forecastday) {
            binding.apply {
                day.text = item.date.toDay()

                humidity.text =
                    "${item.day.avgHumidity} ${context.getString(R.string.percent_symbol)}"

                conditionIcon.setConditionImage(item.day.condition.icon)

                var tempText = ""

                when (tempUnit) {
                    CELSIUS -> {
                        tempText =
                            "${item.day.maxTempC}${context.getString(R.string.degree_symbol)} / ${item.day.minTempC}${
                                context.getString(
                                    R.string.degree_symbol
                                )
                            }"
                    }
                    FAHRENHEIT -> {
                        tempText =
                            "${item.day.maxTempF}${context.getString(R.string.degree_symbol)} / ${item.day.minTempF}${
                                context.getString(
                                    R.string.degree_symbol
                                )
                            }"
                    }
                }

                daysMinMaxTemp.text = tempText

                root.setOnClickListener {
                    onClickListener?.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ForecastListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ForecastListViewHolder(
            ForecastListItemBinding.inflate(
                inflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ForecastListAdapter.ForecastListViewHolder, position: Int
    ) {
        holder.bind(forecastListData!![position])
    }

    override fun getItemCount(): Int {
        return forecastListData?.size ?: 0
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onItemClick(forecastDay: Forecastday)
    }

}
