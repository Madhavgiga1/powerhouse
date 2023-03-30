package com.example.powerhouseevaluation.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powerhouseevaluation.R
import com.example.powerhouseevaluation.Utils.Constants
import com.example.powerhouseevaluation.Utils.NetworkResult
import com.example.powerhouseevaluation.adapters.DayForecastadapter
import com.example.powerhouseevaluation.databinding.FragmentCurrentLocationBinding
import com.example.powerhouseevaluation.viewmodels.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CurrentLocationFragment : Fragment() {
    private var _binding:FragmentCurrentLocationBinding?=null
    private val binding get() = _binding!!
    private lateinit var mainViewModel:MainViewModel
    private val mAdapter by lazy { DayForecastadapter() }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0
    var city:String = "Mumbai"
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
        requestLocationPermission()
        setupRecyclerView()
        if(mainViewModel.hasInternetConnection()){
            requestApiDataforecast()
        }
        else{
            loadDataFromCache()
        }

        setupRecyclerView()

        return binding.root
    }
    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readForecast.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.forecasts=database.first().forecastDay.forecast.forecastday
                }
                else{
                    Toast.makeText(context,"No intetentConnection",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun requestApiDataforecast() {
        mainViewModel.getForecast(applyQueries())
        mainViewModel.forecastResponse.observe(viewLifecycleOwner,{response ->
            when(response){
                is NetworkResult.Success -> {
                    response.data?.let {
                        mAdapter.forecasts=it.forecast.forecastday

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
    private fun showRationalDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showRationalDialog("Powerhouse App", "To use this feature you need to allow the access to the Location")
            }
        } else {
            requestPermissionLocation.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
    @SuppressLint("MissingPermission")
    private val requestPermissionLocation: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            if (!isLocationEnabled()) {
                Toast.makeText(requireContext(), "Your location provider is turned off. Please turn it on.", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            } else {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (addresses != null) {
                                if (addresses.isNotEmpty()) {
                                    city = addresses[0].locality
                                    binding.citylocation.text=city
                                }
                            }
                        }
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val location = task.result

                        }
                    }
            }
        } else {
            Toast.makeText(requireContext(), "Oops, you just denied the permission.", Toast.LENGTH_LONG).show()
        }
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