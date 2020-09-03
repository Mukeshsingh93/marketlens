package com.rs.marketlens.loginview

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rs.marketlens.util.SharedPreferenceValue

class LoginViewModel( val sharedPreferences: SharedPreferenceValue
) : ViewModel() {



   /* class Factory(
        val sharedPreferences: SharedPreferences,
        application: Application
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(sharedPreferences) as T
        }
    }
*/
    class LoginViewModelFactory(
       val sharedPreferences: SharedPreferenceValue
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(sharedPreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}