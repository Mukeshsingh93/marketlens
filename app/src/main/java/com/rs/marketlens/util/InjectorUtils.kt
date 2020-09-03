package com.rs.marketlens.util

import android.content.Context
import android.content.SharedPreferences
import com.rs.marketlens.loginview.LoginViewModel

object InjectorUtils {
   // lateinit var sharedPreferences: SharedPreferences

    private fun getGardenPlantingRepository(context: Context): SharedPreferenceValue {
        return SharedPreferenceValue.getInstance(context)
    }

    fun providePlantListViewModelFactory(fragment: Context): LoginViewModel.LoginViewModelFactory {
        return LoginViewModel.LoginViewModelFactory(getGardenPlantingRepository(fragment))
    }
}