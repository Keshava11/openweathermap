package com.talkcharge.weather.forecast.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.talkcharge.weather.R
import com.talkcharge.weather.common.model.Hourly
import com.talkcharge.weather.util.LogUtils

class HourlyDataAdapter(private val dataSet: ArrayList<Hourly>) : RecyclerView.Adapter<HourlyDataAdapter.ViewHolder>() {

    fun setDataSet(hourlyDataSet: ArrayList<Hourly>?) {

        if (hourlyDataSet != null && hourlyDataSet.isNotEmpty()) {
            dataSet.clear()
            dataSet.addAll(hourlyDataSet)
            this.notifyDataSetChanged()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTxv: TextView
        val gistImv: AppCompatImageView
        val tempTxv: TextView

        init {
            timeTxv = view.findViewById(R.id.hourly_item_time_txv)
            gistImv = view.findViewById(R.id.hourly_item_gist_imv)
            tempTxv = view.findViewById(R.id.hourly_item_temp_txv)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.view_item_hourly_data, viewGroup,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            val hourly = dataSet[position]
            viewHolder.timeTxv.text = hourly.hourlyTimeStr
            viewHolder.tempTxv.text = hourly.tempWUnits

            Picasso.get()
                .load(hourly.iconUrl)
                .error(R.drawable.ic_weather_cloud)
                .placeholder(R.drawable.ic_weather_cloud)
                .into(viewHolder.gistImv)

        } catch (ex: Exception) {
            ex.printStackTrace()
            LogUtils.d("TAG", "Error occurred loading hourly data")
        }

    }

    override fun getItemCount() = dataSet.size

}