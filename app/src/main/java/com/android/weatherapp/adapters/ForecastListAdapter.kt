package com.android.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ForecastListItemBinding
import com.android.weatherapp.models.forecast.Forecastday
import com.android.weatherapp.utils.setConditionImage
import com.android.weatherapp.utils.toDay

class ForecastListAdapter() : RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder>() {

    private val forecastListData = mutableListOf<Forecastday>()

    fun setList(list: List<Forecastday>) {
        forecastListData.removeAll(list)
        forecastListData.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }

    inner class ForecastListViewHolder(private val binding: ForecastListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(item: Forecastday) {
            binding.apply {
                day.text = item.date.toDay()

                humidity.text =
                    "${item.day.avgHumidity} ${context.getString(R.string.percent_symbol)}"

                conditionIcon.setConditionImage(item.day.condition.icon)
                daysMinMaxTemp.text =
                    "${item.day.maxTempC}${context.getString(R.string.degree_symbol)} / ${item.day.minTempC}${
                        context.getString(
                            R.string.degree_symbol
                        )
                    }"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ForecastListViewHolder(
            ForecastListItemBinding.inflate(
                inflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ForecastListAdapter.ForecastListViewHolder,
        position: Int
    ) {
        holder.bind(forecastListData[position])
    }

    override fun getItemCount(): Int {
        return forecastListData.size
    }

}
