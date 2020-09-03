package com.rs.marketlens.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rs.marketlens.R

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        // binding.viewModel = viewModel
          Handler().postDelayed({
               val inten = Intent(this@SplashActivity, LoginActivity::class.java)
              startActivity(inten)
              finish()
           }, 3000)
    }
}