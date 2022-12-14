package com.android.weatherapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.databinding.LocationListItemBinding

class LocationListAdapter : RecyclerView.Adapter<LocationListAdapter.LocationListViewHolder>() {

    private var locationList: List<String>? = null
    private var onClickListener: OnClickListener? = null

    fun setList(locationList: List<String>) {
        this.locationList = locationList
        notifyItemRangeChanged(0, locationList.size)
    }

    inner class LocationListViewHolder(private val binding: LocationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.apply {
                binding.locationText.text = item
                root.setOnClickListener {
                    onClickListener?.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): LocationListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationListViewHolder(
            LocationListItemBinding.inflate(
                inflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: LocationListViewHolder, position: Int
    ) {
        holder.bind(locationList!![position])
    }

    override fun getItemCount(): Int {
        return locationList?.size ?: 0
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onItemClick(location: String)
    }
}