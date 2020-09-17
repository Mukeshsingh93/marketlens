package com.rs.marketlens.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.MarketLensApi
import com.rs.marketlens.util.Repository
import com.rs.marketlens.util.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel (application: Application, val repository: Repository
) : AndroidViewModel(application){

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

    private val _fname = MutableLiveData<String>()
    val fname : LiveData<String?>
        get() = _fname

    private val _lname = MutableLiveData<String>()
    val lname : LiveData<String?>
        get() = _lname

    private val _email = MutableLiveData<String>()
    val email : LiveData<String?>
        get() = _email

    init {

        Log.e("APIRESPONSE","response value is......"+repository.getValue(Constant.USERID))

        _fname.value =  ""
        _lname.value =  ""
        _email.value =  ""
        _navigateActivity.value=0
    }

    fun regAccount() {
        Log.e("APIRESPONSE","response email value value is......"+_email.value.toString())

        when {
            _fname.value!!.isEmpty() -> _message.value = "The first name is required."
            _lname.value!!.isEmpty() -> _message.value = "The last name is required."
            _email.value!!.isEmpty() -> _message.value = "The email is required."
            _email.value!!.isNotEmpty() && !ValidationUtil.isEmailValid(_email.value.toString()) -> _message.value ="The Email address is not valid"
            else -> regApi()
        }
    }

    fun onTextChangedFName(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _fname.value= s.toString()
        }
        else
        {
            _fname.value=""
        }
    }

    fun onTextChangedLName(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _lname.value= s.toString()
        }
        else
        {
            _lname.value=""
        }
    }

    fun onTextChangedEmail(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _email.value= s.toString()
        }
        else
        {
            _email.value=""
        }
    }

    fun regApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= "No internet"
        }
        else
        {
            Log.e(Constant.APIRESPONSE,"Update Verification otp is view...")
            coroutineScope.launch {
                var getPropertiesDeferred = MarketLensApi.retrofitService.useReg(repository.getValue(Constant.USERID).toString(),fname.value.toString(),lname.value.toString(),email.value.toString());
                try {
                    _status.value = ApiStatus.LOADING
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"Update Verification response is..."+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status!!)
                    {
                        _message.value= response.message.toString()
                        repository.setValue(Constant.SUCCESS,response.userid.toString())
                        repository.setValue(Constant.USERNAME,fname.value.toString())
                        repository.setValue(Constant.USEREMAIL,email.value.toString())
                        _navigateActivity.postValue(1)

                    }
                    else
                    {
                        _message.value= response.message.toString()
                    }
                    Log.e("APIRESPONSE","api response is...."+response.toString())
                } catch (e: Exception) {
                    Log.e("APIRESPONSE","error is..."+e.message.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    class RegisterViewModelFactory(private val application: Application,
                                val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}