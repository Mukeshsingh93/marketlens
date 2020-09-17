package com.rs.marketlens.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.rs.marketlens.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


    }
    fun startHomeActivity()
    {
        val intent = Intent(this@LoginActivity,HomeActivity::class.java);
        startActivity(intent);
        finish()
    }
}