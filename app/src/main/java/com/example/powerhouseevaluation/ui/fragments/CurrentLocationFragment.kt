package com.example.powerhouseevaluation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powerhouseevaluation.R
import com.example.powerhouseevaluation.Utils.Constants
import com.example.powerhouseevaluation.Utils.NetworkResult
import com.example.powerhouseevaluation.adapters.DayForecastadapter
import com.example.powerhouseevaluation.databinding.FragmentCurrentLocationBinding
import com.example.powerhouseevaluation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CurrentLocationFragment : Fragment() {
    private var _binding:FragmentCurrentLocationBinding?=null
    private val binding get() = _binding!!
    private lateinit var mainViewModel:MainViewModel
    private val mAdapter by lazy { DayForecastadapter() }
    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0
    val queries: HashMap<String, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun setupRecyclerView() {
        binding.forecastRecyclerview.adapter=mAdapter
        binding.forecastRecyclerview.layoutManager= LinearLayoutManager(requireContext())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentLocationBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        requestApiDataforecast()
        setupRecyclerView()

        return binding.root
    }
    private fun requestApiDataforecast() {
        mainViewModel.getForecast(applyQueries())
        mainViewModel.forecastResponse.observe(viewLifecycleOwner,{response ->
            when(response){
                is NetworkResult.Success -> {
                    response.data?.let {
                        mAdapter.forecasts=it.forecast.forecastday
                        //resultBundle.putParcelable("weatherBundle",it.forecast.forecastday)
                        setupRecyclerView()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    //showShimmerEffect()
                }
            }
        })
    }

    private fun applyQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["key"] = Constants.API_KEY
        queries["q"] = "Jaipur India"
        queries["aqi"]="no"
        queries["days"]="3"
        queries["alerts"]="no"

        return queries
    }


}