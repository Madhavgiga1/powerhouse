package com.example.powerhouseevaluation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.powerhouseevaluation.databinding.CityInfoLayoutBinding
import com.example.powerhouseevaluation.models.ForecastDay

class BonusCityAdapter():RecyclerView.Adapter<BonusCityAdapter.BonusCityViewHolder>() {
    var forecastforcity= emptyList<ForecastDay>()
    class BonusCityViewHolder(private val binding: CityInfoLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(indivcityfrecast: ForecastDay){
            binding.cityforecast=indivcityfrecast
            binding.cityforecastx=indivcityfrecast.forecast.forecastday.first()
            binding.executePendingBindings()

        }
        companion object{
            fun from(parent: ViewGroup):BonusCityAdapter.BonusCityViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding=CityInfoLayoutBinding.inflate(layoutInflater,parent,false)
                return BonusCityViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusCityViewHolder {
        return BonusCityViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BonusCityViewHolder, position: Int) {
        var currentcityforecast=forecastforcity[position]
        holder.bind(currentcityforecast)
    }

    override fun getItemCount(): Int {
        return forecastforcity.size
    }
}