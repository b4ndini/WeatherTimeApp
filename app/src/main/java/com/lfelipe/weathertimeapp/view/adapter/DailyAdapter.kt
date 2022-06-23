package com.lfelipe.weathertimeapp.view.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.model.ForecastX
import com.lfelipe.weathertimeapp.util.Constants
import com.lfelipe.weathertimeapp.util.formatToPtBrDate
import com.lfelipe.weathertimeapp.util.getDayOfWeekInText
import java.time.format.DateTimeFormatter
import java.util.*

class DailyAdapter(
    private val list: List<ForecastX>
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(forecast: ForecastX, pos: Int) = with(itemView) {

            when (pos) {
                0 -> {
                    findViewById<TextView>(R.id.tvDayOfWeek).text = "Hoje"
                }
                1 -> {
                    findViewById<TextView>(R.id.tvDayOfWeek).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    findViewById<TextView>(R.id.tvDayOfWeek).text = "Amanhã"
                }
                else -> {
                    findViewById<TextView>(R.id.tvDayOfWeek).text = forecast.date.formatToPtBrDate()
                }
            }
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val locale = Locale("pt", "BR")
            findViewById<TextView>(R.id.tvDayOfWeekText).text =
                forecast.date.getDayOfWeekInText(locale, DateTimeFormatter.ISO_LOCAL_DATE)
            //  }
            val image = Constants.FORECA_IMAGE_URL + forecast.symbol + ".png"
            Glide.with(this).load(image).into(findViewById(R.id.ivWeatherIcon))
            findViewById<TextView>(R.id.tvMinTemp).text = forecast.minTemp.toString() + "º"
            findViewById<TextView>(R.id.tvMaxTemp).text = forecast.maxTemp.toString() + "º"
            findViewById<TextView>(R.id.tvPrecipAccum).text =
                forecast.precipAccum.toString() + " mm"
        }

    }

}
