package com.lfelipe.weathertimeapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.model.ForecastXX
import com.lfelipe.weathertimeapp.util.Constants
import com.lfelipe.weathertimeapp.util.formatHour

class HourlyAdapter (
    private val list: List<ForecastXX>
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(forecast: ForecastXX) = with(itemView) {

            val image = Constants.FORECA_IMAGE_URL + forecast.symbol + ".png"
            Glide.with(this).load(image).into(findViewById(R.id.ivHourlyWeatherIcon))
            findViewById<TextView>(R.id.tvHour).text = forecast.time.formatHour()
            findViewById<TextView>(R.id.tvHourlyTempAndPrecip).text = "${forecast.temperature}º   Prob. Chuva: ${forecast.precipProb}%"

        }

    }

}
