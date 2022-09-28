package com.talkcharge.weather.forecast.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.talkcharge.weather.R
import com.talkcharge.weather.common.model.Daily
import com.talkcharge.weather.util.LogUtils

class DailyDataAdapter(private val dataSet: ArrayList<Daily>) : RecyclerView.Adapter<DailyDataAdapter.ViewHolder>() {

    fun setDataSet(dailyDataSet: ArrayList<Daily>?) {

        if (dailyDataSet != null && dailyDataSet.isNotEmpty()) {
            dataSet.clear()
            dataSet.addAll(dailyDataSet)
            this.notifyDataSetChanged()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTxv: TextView
        val gistImv: AppCompatImageView
        val tempTxv: TextView

        init {
            timeTxv = view.findViewById(R.id.daily_item_time_txv)
            gistImv = view.findViewById(R.id.daily_item_gist_imv)
            tempTxv = view.findViewById(R.id.daily_item_temp_txv)
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.view_item_daily_data, viewGroup, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            val dailyData = dataSet[position]
            viewHolder.timeTxv.text = dailyData.dayTimeStr
            viewHolder.tempTxv.text = dailyData.dailyTempWUnits

            Picasso.get()
                .load(dailyData.iconUrl)
                .error(R.drawable.ic_weather_cloud)
                .placeholder(R.drawable.ic_weather_cloud)
                .into(viewHolder.gistImv)

        } catch (ex: Exception) {
            ex.printStackTrace()
            LogUtils.d("TAG", "Error occurred loading daily data")
        }

    }

    override fun getItemCount() = dataSet.size

}