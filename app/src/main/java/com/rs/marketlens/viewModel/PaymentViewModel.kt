package com.rs.marketlens.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.MarketLensApi
import com.rs.marketlens.util.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PaymentViewModel (application: Application, val repository: SharedPreferences
) : AndroidViewModel(application){

    val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope( viewmodelJob+ Dispatchers.Main )
    private val context = getApplication<Application>().applicationContext

    var userid:String=""

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

    init {
        _phnNo.value =  ""
        _navigateActivity.value=0
    }


    fun getPaymentid(paymentid:String)
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            Log.e("APIRESPONSE","check userid is..."+repository.getString(Constant.USERID,"").toString())
            var userid :String= repository.getString(Constant.USERID,"").toString()
            var orderid :String= ""
            if(repository.getString(Constant.PAYMENTTYPE,"").toString().equals(Constant.WEEKLYPACKAGEORDERID))
            {
                 orderid = repository.getString(Constant.WEEKLYPACKAGEORDERID,"").toString()

            }
            else
            {
                 orderid = repository.getString(Constant.PACKAGEORDERID,"").toString()

            }

            Log.e(Constant.APIRESPONSE,"getPaymentid  api id id is..."+userid)
            Log.e(Constant.APIRESPONSE,"getPaymentid  api orderid is..."+orderid.toString())
            Log.e(Constant.APIRESPONSE,"getPaymentid  api  orderid is..."+orderid.toString())
            Log.e(Constant.APIRESPONSE,"getPaymentid  api  orderid is..."+paymentid.toString())

            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.paymentSuccess(userid.toString(),orderid.toString(),"RazorPay",paymentid.toString(),paymentid.toString(),"success");
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"Update getPaymentid api response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                     //   repository.setValue(Constant.PACKAGEORDERID,response.orderid.toString())
                        _message.value= response.message.toString()

                        if(repository.getString(Constant.PAYMENTTYPE,"").toString().equals(Constant.WEEKLYPACKAGEORDERID))
                        {
                            repository.edit {
                                putString(Constant.WEEKLYPACKAGEORDERID,"")
                            }
                        }
                        else
                        {
                            repository.edit {
                                putString(Constant.PACKAGEORDERID,"")
                            }
                        }
                        _navigateActivity.value=1
                        //
                    }
                    else
                    {
                        _message.value= response.message.toString()
                    }
                } catch (e: Exception) {
                    Log.e("APIRESPONSE","getPaymentid error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }







}