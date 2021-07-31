package com.lfelipe.weathertimeapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfelipe.util.Constants
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.model.Forecast


class MainAdapter (
    private val list: List<Forecast>
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(forecast: Forecast) = with(itemView) {


            val image = Constants.FORECA_IMAGE_URL + forecast.symbol + ".png"
            Glide.with(this).load(image).into(findViewById(R.id.ivWeatherIcon))
            findViewById<TextView>(R.id.tvMinMaxTemp).text = forecast.minTemp.toString()+"ºC/" +forecast.maxTemp.toString()+"ºC"
            findViewById<TextView>(R.id.tvPrepRate).text = forecast.precipAccum.toString()+" mm"
        }

    }

}
