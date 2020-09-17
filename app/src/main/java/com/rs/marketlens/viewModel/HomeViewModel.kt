package com.rs.marketlens.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.MarketLensApi
import com.rs.marketlens.network.*
import com.rs.marketlens.util.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(application: Application, val repository: Repository
) : AndroidViewModel(application) {

    val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope( viewmodelJob+ Dispatchers.Main )
    private val context = getApplication<Application>().applicationContext

    var otp:String="2345"

    private val _message = MutableLiveData<String>()
    val message: LiveData<String?>
        get() = _message

    private val _phnNo = MutableLiveData<String>()
    val phnNo : LiveData<String?>
        get() = _phnNo

    private val _navigateActivity = MutableLiveData<Int>()
    val navigateActivity : LiveData<Int?>
        get() = _navigateActivity


    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _latestvideos = MutableLiveData<List<LatestVideos>>()
    val latestvideos: LiveData<List<LatestVideos>>
        get() = _latestvideos
    var latestvideo: MutableList<LatestVideos>? =  mutableListOf()


    private val _featuredvideos = MutableLiveData<List<FeaturedVideos>>()
    val featuredvideos: LiveData<List<FeaturedVideos>>
        get() = _featuredvideos

    private val _homeslider = MutableLiveData<List<HomeSlider>>()
    val homeslider: LiveData<List<HomeSlider>>
        get() = _homeslider
    var homesliders: MutableList<HomeSlider>? =  mutableListOf()


    private val _ourservice = MutableLiveData<List<OurService>>()
    val ourservice: LiveData<List<OurService>>
        get() = _ourservice
    var ourservices: MutableList<OurService>? =  mutableListOf()




    init {
        _phnNo.value =  ""
        _navigateActivity.value=0

        ourservices!!.add(OurService(Constant.ONLINECOURSES))
        ourservices!!.add(OurService(Constant.WEEKLYSTRATEGY))
        ourservices!!.add(OurService(Constant.MLCALCULATOR))
        _ourservice.value =  ourservices

      //  latestvideo!!.add(LatestVideos("1",""))
      //  latestvideo!!.add(LatestVideos("2",""))
      //  latestvideo!!.add(LatestVideos("3",""))
      //  _latestvideos.value =  latestvideo


     //   homesliders!!.add(HomeSlider("1",""))
    //    homesliders!!.add(HomeSlider("2",""))
   //     homesliders!!.add(HomeSlider("3",""))
      //  _homeslider.value =  homesliders

        //   ourservices!!.add(OurService(Constant.WEEKLYSTRATEGY))
     //   ourservices!!.add(OurService(Constant.WEEKLYSTRATEGY))

        Log.e("SLIDERIMAGES","slider images is...."+_ourservice.value!!.get(0).toString())
       dashboardApi()
    }

    class HomeViewModelFactory(private val application: Application,
                                val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun onTextChangedPhone(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _phnNo.value= s.toString()
        }
        else
        {
            _phnNo.value=""
        }
    }

    fun complete()
    {
        _navigateActivity.value=0
    }

    fun dashboardApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {

            Log.e(Constant.APIRESPONSE,"dashboard api is is view..."+repository.getValue(Constant.USERID).toString())
            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.getDashboard(repository.getValue(Constant.USERID).toString())

                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"dashboard api response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                        _homeslider.value=response.sliders
                   //     _featuredvideos.value=response.featuredvideos
                        _latestvideos.value=response.latestvideos
                        //
                    }
                    else
                    {
                        _message.value= response.message.toString()
                    }
                } catch (e: Exception) {
                    Log.e("APIRESPONSE","dashboard error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

}