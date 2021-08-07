package com.lfelipe.weathertimeapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lfelipe.weathertimeapp.R
import com.lfelipe.weathertimeapp.model.LocationX

class SearchAdapter(
    private val list: List<LocationX>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(location: LocationX) = with(itemView) {


            if(location.country == "Brazil" && location.adminArea != null && location.adminArea.substring(0,6) == "Estado" ){
                    val estado = location.adminArea.substring(10, location.adminArea.length)
                    findViewById<TextView>(R.id.tvCity).text = location.name + " - " + estado
            }else{
                findViewById<TextView>(R.id.tvCity).text = location.name + " - " + location.adminArea
            }
            findViewById<TextView>(R.id.tvCountry).text = location.country

        }

    }

}
