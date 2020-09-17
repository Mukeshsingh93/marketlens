package com.rs.marketlens.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.util.Constant
import com.google.android.material.navigation.NavigationView
import com.razorpay.PaymentResultListener
import com.rs.marketlens.R
import com.rs.marketlens.databinding.HomeActivityBinding
import com.rs.marketlens.views.PaymentGatWay
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), PaymentResultListener {

    var TAG = "PAYMENTTAG"

    private lateinit var navController: NavController
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.home_activity
        )
        sharedPreferences =
            (this).getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        //  setContentView(R.layout.activity_home)
        navController = Navigation.findNavController(this, R.id.fragment)
        val navController = Navigation.findNavController(this@HomeActivity, R.id.fragment)

      binding.drawer.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        val navigationView = findViewById(R.id.navigation_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val onlinecourse = headerView.findViewById(R.id.onlinecourse) as RelativeLayout
        val weeklycourse = headerView.findViewById(R.id.weeklycourse) as RelativeLayout
        val aboutus = headerView.findViewById(R.id.aboutus) as RelativeLayout
        val nametext = headerView.findViewById(R.id.nametext) as TextView
        val userphoneno = headerView.findViewById(R.id.userphoneno) as TextView
        val needhelp = headerView.findViewById(R.id.needhelp) as RelativeLayout
        val privacypolicy = headerView.findViewById(R.id.privacypolicy) as RelativeLayout
        val termcondition = headerView.findViewById(R.id.termcondition) as RelativeLayout
        val logout = headerView.findViewById(R.id.logout) as RelativeLayout
        val notification = headerView.findViewById(R.id.notification) as RelativeLayout
        val shareus = headerView.findViewById(R.id.shareus) as RelativeLayout

        nametext.text= sharedPreferences.getString(Constant.USERNAME,"").toString()
        userphoneno.text= "+91 "+sharedPreferences.getString(Constant.USERMOBILENO,"").toString()
      /*  val blog = headerView.findViewById(R.id.blog) as TextView
        val disclaimerpolicy = headerView.findViewById(R.id.disclaimerpolicy) as TextView
        val termofuse = headerView.findViewById(R.id.termofuse) as TextView
        val privacypolicy = headerView.findViewById(R.id.privacypolicy) as TextView
        val returncancellation = headerView.findViewById(R.id.returncancellation) as TextView*/
      /*  onlinecourse.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val navController: NavController=Navigation.findNavController(this, R.id.fragment);
                navController.navigate(R.id.sliderCourses, bundleOf("type" to Constant.ONLINECOURSES));
            }, 300)
        }
        weeklycourse.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val navController: NavController=Navigation.findNavController(this, R.id.fragment);
                navController.navigate(R.id.sliderCourses, bundleOf("type" to Constant.WEEKLYSTRATEGY));
            }, 300)
        }*/

        aboutus.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val navController: NavController=Navigation.findNavController(this, R.id.fragment);
                navController.navigate(R.id.webViewFragment, bundleOf("url" to "https://www.marketlens.in/about-us.html"));
            }, 300)
        }
        needhelp.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({

                val navController: NavController=Navigation.findNavController(this, R.id.fragment);
                navController.navigate(R.id.webViewFragment, bundleOf("url" to "https://www.marketlens.in/contact-us.html"));

            }, 300)
        }
        privacypolicy.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({

                val navController: NavController=Navigation.findNavController(this, R.id.fragment);
                navController.navigate(R.id.webViewFragment, bundleOf("url" to "https://www.marketlens.in/privacy-policy.html"));

            }, 300)
        }
        termcondition.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({

                val navController: NavController=Navigation.findNavController(this, R.id.fragment);
                navController.navigate(R.id.webViewFragment, bundleOf("url" to "https://www.marketlens.in/terms-and-conditions.html"));

            }, 300)
        }

        logout.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({

                sharedPreferences.edit().clear().commit()
                val intent = Intent(this@HomeActivity,LoginActivity::class.java);
                startActivity(intent)
                finish();

            }, 300)
        }
        notification.setOnClickListener {
          //  drawerLayout.closeDrawers();
            Handler().postDelayed({
               Toast.makeText(this,"No Notification(s) Found",Toast.LENGTH_SHORT).show()
            }, 300)
        }
        shareus.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + "com.rs.marketlens" + " APP"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }, 300)
        }
    }

    fun showToolpBar()
    {
        binding.topview.visibility= View.VISIBLE
    }

    fun paymentgateway()
    {
        val intent = Intent(this@HomeActivity,PaymentGatWay::class.java);
        startActivity(intent);
    }

    fun webviewOpen(url:String)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
        startActivity(browserIntent)
    }


    fun paymentSuccessfull()
    {
        val intent = Intent(this@HomeActivity,HomeActivity::class.java);
        startActivity(intent)
        finish();
    }
    fun hideToolpBar()
    {
        binding.topview.visibility=View.GONE

    }

    override fun onPaymentSuccess(razorpayPaymentID: String) {
        try {
            Log.e(TAG, "Payment Successful: $razorpayPaymentID")
        } catch (e: Exception) {
            Log.e(TAG, "Payment UnSuccessful: " + e.message.toString())
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentError(i: Int, s: String) {


        Log.e(
            TAG,
            "Exception in onPaymentSuccess....$i....sss.....$s"
        )
        (this as PaymentGatWay).method()

    }
}