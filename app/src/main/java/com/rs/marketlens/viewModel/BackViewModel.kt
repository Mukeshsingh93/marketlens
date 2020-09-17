package com.rs.marketlens.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.MarketLensApi
import com.rs.marketlens.util.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BackViewModel (val sharedPreferences: SharedPreferences,
                     application: Application
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

    init {
        //  sliderimage!!.add(PropertiesImages("D"))
        _phnNo.value =  ""
        //  signupApi()
        _navigateActivity.value=0
    }

    class LoginViewModelFactory(private val application: Application,
                                val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    fun signinApi()
    {
        if(!Constant.connected(context))
        {
           // _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
             var token=sharedPreferences.getString(Constant.ACCESSTOKEN,"")

            //  _status.value = ApiStatus.LOADING
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                var getPropertiesDeferred = MarketLensApi.retrofitService.userLog("998888555888")
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    Log.e("APIRESPONSE","api response is called...."+response.toString())
                    // if(response.Status== Constant.SUCCEESSSTATUS)
                    //  {
                    // sharedPreferences.edit { putString(Constant.USERMOBILENO,phnNo.value.toString()) }
                    //   sharedPreferences.edit { putString(Constant.USERID,response.SignUpID.toString()) }

                    //   _message.value= response.message.toString()
                    //    otp= response.OTP.toString()
                    //     _navigateActivity.postValue(1)
                    //  }
                    //  else
                    // {
                    _message.value= response.message.toString()
                    // }
                    //   sharedPreferences.edit { putString(Constant.ACCESSTOKEN, response.access_token!!.toString()) }

                } catch (e: Exception) {
                    Log.e("APIRESPONSE","error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }
}