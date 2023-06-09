package com.example.powerhouseevaluation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.powerhouseevaluation.Utils.NetworkResult
import com.example.powerhouseevaluation.data.Repository
import com.example.powerhouseevaluation.data.database.ForecastEntity
import com.example.powerhouseevaluation.models.ForecastDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository,application: Application):AndroidViewModel(application){
    // using a combination of LiveData and NetworkResult, we can observe changes to the result of the network request and handle them appropriately in the UI layer. For example, we can show a loading indicator while the request is being made, display an error message if the request fails, and show the forecast data if the request is successful.
    var forecastResponse: MutableLiveData<NetworkResult<ForecastDay>> = MutableLiveData()
    var allchannels= ArrayList<ForecastDay>()

    val readForecast: LiveData<List<ForecastEntity>> = repository.local.readDatabase().asLiveData()
    private fun insertForecast(channelEntity: ForecastEntity)=
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertChannels(channelEntity)
        }
    private fun offlinecacheChannels(forecastcache: ForecastDay) {
        val channelEntity=ForecastEntity(forecastcache)
        insertForecast(channelEntity)

    }



    fun getForecast(queries:Map<String,String>)=viewModelScope.launch{
        getForecastSafeCall(queries)
    }

    private suspend fun getForecastSafeCall(queries: Map<String, String>) {
        forecastResponse.value=NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response=repository.remote.getForecast(queries)//the response received from retrofit
                forecastResponse.value=handleForecastResponse(response)
                val forecastcache=forecastResponse.value!!.data
                if(forecastcache!=null){
                    offlinecacheChannels(forecastcache)
                }
            }catch (e: Exception){
                forecastResponse.value = NetworkResult.Error("Weather error not found.")
            }
        }
        else{
            forecastResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleForecastResponse(response: Response<ForecastDay>): NetworkResult<ForecastDay> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                if(foodRecipes != null) {
                    return NetworkResult.Success(foodRecipes)
                } else {
                    return NetworkResult.Error("No data found.")
                }
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


     fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}