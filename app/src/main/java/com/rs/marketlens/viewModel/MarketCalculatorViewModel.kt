package com.rs.marketlens.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.MarketLensApi
import com.rs.marketlens.network.MarketLensCalculator
import com.rs.marketlens.network.OurService
import com.rs.marketlens.util.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MarketCalculatorViewModel (application: Application, val repository: Repository
) : AndroidViewModel(application) {

    val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope( viewmodelJob+ Dispatchers.Main )
    private val context = getApplication<Application>().applicationContext


    private val _message = MutableLiveData<String>()
    val message: LiveData<String?>
        get() = _message


    private val _navigateActivity = MutableLiveData<Int>()
    val navigateActivity : LiveData<Int?>
        get() = _navigateActivity

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _mlenscalculator = MutableLiveData<List<MarketLensCalculator>>()
    val mlenscalculator: LiveData<List<MarketLensCalculator>>
        get() = _mlenscalculator

    init {
        getCalculatorApi()
        _navigateActivity.value=0
    }



    class MarketCalculatorViewModelFactory(private val application: Application,
                                val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MarketCalculatorViewModel::class.java)) {
                return MarketCalculatorViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }





    fun complete()
    {
        _navigateActivity.value=0
    }

    fun getCalculatorApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            Log.e("APIRESPONSE","check userid is..."+repository.getValue(Constant.USERID).toString())
            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.marketlensCalculator(repository.getValue(Constant.USERID).toString());
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"Update getvideos api response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                        _mlenscalculator.value= response.mlenscalculator
                    }
                    else
                    {
                        _message.value= response.message.toString()
                    }
                } catch (e: Exception) {
                    Log.e("APIRESPONSE","error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }
}