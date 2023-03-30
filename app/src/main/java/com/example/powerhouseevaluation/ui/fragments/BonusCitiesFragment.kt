package com.example.powerhouseevaluation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powerhouseevaluation.Utils.Constants
import com.example.powerhouseevaluation.Utils.NetworkResult
import com.example.powerhouseevaluation.adapters.BonusCityAdapter
import com.example.powerhouseevaluation.databinding.FragmentBonusCitiesBinding
import com.example.powerhouseevaluation.models.ForecastDay
import com.example.powerhouseevaluation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BonusCitiesFragment : Fragment() {
    private var _binding: FragmentBonusCitiesBinding? = null
    private val binding get() = _binding!!
    lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { BonusCityAdapter() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mainViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentBonusCitiesBinding.inflate(layoutInflater, container, false)
        prereq()
        setupRecyclerView()
        return binding.root
    }

    private fun prereq() {
        var citylist = listOf("New York", "Singapore", "Mumbai", "Delhi", "Sydney", "Melbourne")
        citylist.forEach {
            requestAPIdata(applyQueries(it), it)
        }

    }

    private fun requestAPIdata(applyQueries: HashMap<String, String>, it: String) {
        mainViewModel.getForecast(applyQueries)
        mainViewModel.forecastResponse.observe(viewLifecycleOwner,{ response->
            when(response){
                is NetworkResult.Success ->{
                    response.data?.let {
                        if(!(mainViewModel.allchannels.contains(it))){
                            mainViewModel.allchannels.add(it)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        })

    }

    private fun setupRecyclerView() {
        mAdapter.forecastforcity = mainViewModel.allchannels
        binding.rv.adapter = mAdapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun applyQueries(string: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["key"] = Constants.API_KEY
        queries["q"] = string
        queries["aqi"] = "no"
        queries["days"] = "1"
        queries["alerts"] = "no"

        return queries
    }
}
