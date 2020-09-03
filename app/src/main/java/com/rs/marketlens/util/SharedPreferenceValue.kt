package com.rs.marketlens.util

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.util.Log
import kotlin.reflect.jvm.internal.impl.load.java.Constant

class SharedPreferenceValue private constructor(private val data: Context) {


    companion object {
        // For Singleton instantiation
        @Volatile private var instance: SharedPreferenceValue? = null
        fun getInstance(gardenPlantingDao: Context) =
            instance ?: synchronized(this) {
                instance ?: SharedPreferenceValue(gardenPlantingDao).also {
                    instance = it }
            }
    }

}