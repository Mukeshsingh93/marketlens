package com.rs.marketlens.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.MarketLensApi
import com.rs.marketlens.network.MontlyPlan
import com.rs.marketlens.network.OurServiceVideos
import com.rs.marketlens.network.PackageDetails
import com.rs.marketlens.util.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OnlineCoursesViewModel (application: Application, val repository: Repository
) : AndroidViewModel(application){

    val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope( viewmodelJob+ Dispatchers.Main )
    private val context = getApplication<Application>().applicationContext

    var userid:String=""

    private val _message = MutableLiveData<String>()
    val message: LiveData<String?>
        get() = _message

    private val _packageDetail= MutableLiveData<PackageDetails>()
    val packageDetail: LiveData<PackageDetails?>
        get() = _packageDetail

    private val _navigateActivity = MutableLiveData<Int>()
    val navigateActivity : LiveData<Int?>
        get() = _navigateActivity


    private val _notify = MutableLiveData<Int>()
    val notify : LiveData<Int?>
        get() = _notify

    private val _subsPeriod = MutableLiveData<List<MontlyPlan>>()
    val subsPeriod : LiveData<List<MontlyPlan>?>
        get() = _subsPeriod
    var subsPeriods: MutableList<MontlyPlan>? =  mutableListOf()


    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _viewVisible= MutableLiveData<Int>()
    val viewVisible: LiveData<Int?>
        get() = _viewVisible

    private val _sliderimg = MutableLiveData<List<OurServiceVideos>>()
    val sliderimg: LiveData<List<OurServiceVideos>>
        get() = _sliderimg
    var sliderimage: MutableList<OurServiceVideos>? =  mutableListOf()


    init {
        _notify.value=0
        _viewVisible.value=0
     //   getLatestVideoApi()

        /*      sliderimage!!.add(OurService("X"))
              sliderimage!!.add(OurService("X"))
              sliderimage!!.add(OurService("X"))
              sliderimage!!.add(OurService("X"))
              sliderimage!!.add(OurService(""))
              sliderimage!!.add(OurService("Relapsing Polychondritis"))
              sliderimage!!.add(OurService("Celiac Disease"))
              sliderimage!!.add(OurService("Allergies & Asthma"))
              sliderimage!!.add(OurService("RHEUMATOID ARTHRITIS"))*/
//        subsPeriods!!.add(MontlyPlan("","","Montly"))
//        subsPeriods!!.add(MontlyPlan("","","Yearly"))
//        subsPeriods!!.add(MontlyPlan("","","Weekly"))
//       _subsPeriod.value =  subsPeriods
     //   _navigateActivity.value=0
    }
    fun complete()
    {
        _notify.value=0
        _navigateActivity.value=0
    }

    fun getLatestVideoApi(type:String,servicetype:Int)
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            Log.e("APIRESPONSE","check userid is..."+repository.getValue(Constant.USERID).toString())

            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.getVideos(repository.getValue(Constant.USERID).toString(),type.toString());
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"Update getvideos api response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                        if(servicetype==1)
                        {
                            if(response.packagedetails!!.is_paid!! == 1)
                            {
                                sliderimage!!.addAll(response.latestvideos!!)
                                _sliderimg.value = sliderimage
                            }
                            else
                            {
                                _notify.value=1
                            }
                        }
                        else {

                            if(sliderimage!!.size>0)
                            {
                                sliderimage!!.clear();
                            }
                            _packageDetail.value = response.packagedetails
                            sliderimage!!.addAll(response.latestvideos!!)
                            _sliderimg.value = sliderimage

                            if (type.equals("weekly-courses")) {
                                _subsPeriod.value = response.packagepricing
                            }
                            _notify.value=1
                            _viewVisible.value=1
                        }
                        //
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



    fun getOrderIdApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            //  repository.setValue(Constant.PACKAGEORDERID,"")

            Log.e("APIRESPONSE","check userid is..."+repository.getValue(Constant.USERID).toString())
            var orderid :String= repository.getValue(Constant.PACKAGEORDERID).toString()
            var totalPrice :Int= _packageDetail.value!!.price!!.toInt()
            //var totalPrice :Int= 1
            //var gst:Int = 0
            var gst:Double = totalPrice.times(18).div(100).toDouble()

            var finalTotalPrice:Double=totalPrice.plus(gst).toDouble()
            Log.e(Constant.APIRESPONSE,"finalTotalPrice finalTotalPrice is......"+finalTotalPrice.toString())

            repository.setValue(Constant.FINALAMOUNT,finalTotalPrice.toString())

            Log.e(Constant.APIRESPONSE,"getOrderIdApi  api id id is..."+repository.getValue(Constant.USERID).toString())
            Log.e(Constant.APIRESPONSE,"getOrderIdApi  api category id is..."+_packageDetail.value!!.id.toString())
            Log.e(Constant.APIRESPONSE,"getOrderIdApi  api orderid is..."+orderid.toString())
            Log.e(Constant.APIRESPONSE,"getOrderIdApi  api gst is..."+gst.toString())
            Log.e(Constant.APIRESPONSE,"getOrderIdApi  api totalPrice is..."+totalPrice.toString())
            Log.e(Constant.APIRESPONSE,"getOrderIdApi  api  orderid is..."+orderid.toString())
//order_Fcrq8GLodv8iZC
            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.getOrderId(repository.getValue(Constant.USERID).toString(),_packageDetail.value!!.id.toString(),_packageDetail.value!!.price.toString(),gst.toString(),"",orderid.toString());
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"getOrderIdApi  api response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                        repository.setValue(Constant.PACKAGEORDERID,response.orderid.toString())
                        repository.setValue(Constant.PAYMENTTYPE,Constant.PACKAGEORDERID)

                        _navigateActivity.value=1
                        //
                    }
                    else
                    {
                        _message.value= response.message.toString()
                    }
                } catch (e: Exception) {
                    Log.e("APIRESPONSE","getOrderIdApi error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun getWeeklyOrderIdApi(subid:String,price:String,discount:String)
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            //  repository.setValue(Constant.PACKAGEORDERID,"")
            Log.e("APIRESPONSE","check userid is..."+repository.getValue(Constant.USERID).toString())
            var orderid :String= repository.getValue(Constant.WEEKLYPACKAGEORDERID).toString()
            var totalPrice :Int= price.toString().toInt()
            var gst:Double = totalPrice.times(18).div(100).toDouble()
            var finalTotalPrice:Double=totalPrice.plus(gst).toDouble()
            Log.e(Constant.APIRESPONSE,"finalTotalPrice finalTotalPrice is......"+finalTotalPrice.toString())
            repository.setValue(Constant.FINALAMOUNT,finalTotalPrice.toString())
            Log.e(Constant.APIRESPONSE,"getWeeklyOrderIdApi  api id id is..."+repository.getValue(Constant.USERID).toString())
            Log.e(Constant.APIRESPONSE,"getWeeklyOrderIdApi  api category id is..."+subid.toString())
            Log.e(Constant.APIRESPONSE,"getWeeklyOrderIdApi  api orderid is..."+orderid.toString())
            Log.e(Constant.APIRESPONSE,"getWeeklyOrderIdApi  api gst is..."+gst.toString())
            Log.e(Constant.APIRESPONSE,"getWeeklyOrderIdApi  api totalPrice is..."+totalPrice.toString())
            Log.e(Constant.APIRESPONSE,"getWeeklyOrderIdApi  api  orderid is..."+orderid.toString())

           coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.getOrderId(repository.getValue(Constant.USERID).toString(),subid.toString(),totalPrice.toString(),gst.toString(),discount.toString(),orderid.toString());
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"getOrderIdApi  api response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                        repository.setValue(Constant.WEEKLYPACKAGEORDERID,response.orderid.toString())
                        repository.setValue(Constant.PAYMENTTYPE,Constant.WEEKLYPACKAGEORDERID)
                        _navigateActivity.value=1
                        //
                    }
                    else
                    {
                        _message.value= response.message.toString()
                    }
                } catch (e: Exception) {
                    Log.e("APIRESPONSE","getOrderIdApi error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    class OnlineCoursesViewModelFactory(private val application: Application,
                                val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OnlineCoursesViewModel::class.java)) {
                return OnlineCoursesViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}