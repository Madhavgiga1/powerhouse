package com.example.powerhouseevaluation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.powerhouseevaluation.databinding.DailyForecastLayoutBinding
import com.example.powerhouseevaluation.models.ForecastDay
import com.example.powerhouseevaluation.models.ForecastdayX


class DayForecastadapter():RecyclerView.Adapter<DayForecastadapter.DayForecastViewHolder>() {
    var forecasts= emptyList<ForecastdayX>()
    class DayForecastViewHolder(private val binding: DailyForecastLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(day: ForecastdayX){
            binding.forecastforday=day
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):DayForecastViewHolder{
                val layoutinflater= LayoutInflater.from(parent.context)
                val binding=DailyForecastLayoutBinding.inflate(layoutinflater,parent,false)
                return DayForecastViewHolder(binding)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayForecastViewHolder {
        return DayForecastViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DayForecastViewHolder, position: Int) {
        var currentforecast=forecasts[position]
        holder.bind(currentforecast)
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }
}