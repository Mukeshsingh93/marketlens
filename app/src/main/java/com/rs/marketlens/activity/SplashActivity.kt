package com.rs.marketlens.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.util.Constant
import com.rs.marketlens.R
import com.rs.marketlens.views.FinalPaymentGateways
import com.rs.marketlens.views.PaymentGatWays

class SplashActivity : AppCompatActivity()
{
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)

        // binding.viewModel = viewModel
          Handler().postDelayed({
              if(sharedPreferences.getString(Constant.SUCCESS,"").equals(""))
              {
                  val inten = Intent(this@SplashActivity, LoginActivity::class.java)
                  startActivity(inten)
                  finish()
              }
              else
              {
                  val inten = Intent(this@SplashActivity, HomeActivity::class.java)
                  startActivity(inten)
                  finish()
              }
           }, 3000)
    }
}