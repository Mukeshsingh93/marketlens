package com.rs.marketlens.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.myapplication.util.Constant

class Repository private constructor(private val context: Context) {

    lateinit var  sharedPreferences:SharedPreferences;

    fun getValue(key:String): String?
    {
        sharedPreferences= context.getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key,"")
    }

    fun setValue(key:String,value:String)
    {
        sharedPreferences= context.getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        sharedPreferences.edit { putString(key,value) }
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: Repository? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Repository(context).also {
                    instance = it }
            }
    }

}