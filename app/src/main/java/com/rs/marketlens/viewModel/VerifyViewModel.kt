package com.rs.marketlens.viewModel

import android.app.Application
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

class VerifyViewModel (application: Application, val repository: Repository
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

    fun checkOtpApi(otp:String,mobileno:String)
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            repository.setValue(Constant.USERMOBILENO,mobileno.toString())

            Log.e(Constant.APIRESPONSE,"Update Verification otp is view..."+otp.toString())
                coroutineScope.launch {
                    var getPropertiesDeferred = MarketLensApi.retrofitService.verifyOtp(mobileno.toString(),otp.toString());
                    try {
                        _status.value = ApiStatus.LOADING
                        val response = getPropertiesDeferred.await()
                        Log.e(Constant.APIRESPONSE,"Update Verification response is..."+response.toString())
                        _status.value = ApiStatus.DONE
                        if(response.status!!)
                        {
                            userid=response.userid.toString()
                            repository.setValue(Constant.USERID,response.userid.toString())
                            if(response.is_active==0)
                            {
                                _navigateActivity.postValue(1)
                            }
                            else
                            {
                                repository.setValue(Constant.SUCCESS,response.userid.toString())
                                repository.setValue(Constant.USERNAME,response.userdata!!.fname.toString())
                                repository.setValue(Constant.USEREMAIL,response.userdata!!.email.toString())
                                _navigateActivity.postValue(2)

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

   class VerifyOtpModelFactory(private val application: Application,
                                val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VerifyViewModel::class.java)) {
                return VerifyViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }





}