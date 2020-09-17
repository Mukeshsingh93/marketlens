package com.rs.marketlens.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

class LoginViewModel( application: Application,val repository: Repository
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

    private val _termCondition= MutableLiveData<Boolean>()
    val termCondition: LiveData<Boolean>
        get() = _termCondition

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    init {
        _phnNo.value =  ""
        _termCondition.value=false

        _navigateActivity.value=0
    }

    fun setTermCondition(value:Boolean)
    {
        _termCondition.value = value
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

    fun signIn()
    {
        when {
            _phnNo.value.toString().isEmpty()-> _message.value= "Mobile No is required"
            !termCondition.value!! -> _message.value="You need to accept our Terms and Conditions"

            else -> signinApi()
        }
    }

    fun complete()
    {
        _navigateActivity.value=0
    }

    fun signinApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.userLog(phnNo.value.toString())
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    Log.e(Constant.APIRESPONSE,"api login response is called...."+response.toString())
                  if(response.status!!)
                  {
                      Log.e(Constant.APIRESPONSE,"api login response is called....")
                      _navigateActivity.value=1
                      _message.value= response.message.toString()
                  }
                    else
                  {
                      _message.value= response.message.toString()
                  }

                } catch (e: Exception) {
                    Log.e(Constant.APIRESPONSE,"error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }
}