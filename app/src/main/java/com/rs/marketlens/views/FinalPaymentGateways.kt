package com.rs.marketlens.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.rs.marketlens.R
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.util.ProgressDialog
import com.rs.marketlens.viewModel.PaymentViewModel
import com.rs.marketlens.viewModel.PaymentViewModelFactory
import org.json.JSONObject

class FinalPaymentGateways : AppCompatActivity(), PaymentResultWithDataListener {
    var TAG = "PAYMENTTAG"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: PaymentViewModel
    private lateinit var loader: ProgressDialog
    var finalAmount:Double=0.0
    var email:String=""
    var name:String=""
    var mobileno:String=""

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instamojo_payment)
        loader = ProgressDialog(this)

        sharedPreferences = getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        finalAmount=sharedPreferences.getString(Constant.FINALAMOUNT,"")!!.toDouble()
        name=sharedPreferences.getString(Constant.USERNAME,"")!!
        email=sharedPreferences.getString(Constant.USEREMAIL,"")!!
        mobileno=sharedPreferences.getString(Constant.USERMOBILENO,"")!!
        Log.e(Constant.APIRESPONSE,"after after after finalTotalPrice finalTotalPrice is......"+finalAmount.toString())
        Log.e(Constant.APIRESPONSE,"after after after finalTotalPrice email is......"+email.toString())
        Log.e(Constant.APIRESPONSE,"after after after finalTotalPrice mobileno is......"+mobileno.toString())

        /* val application = requireNotNull(this.activity).application
          viewModel: PaymentViewModel by viewModels {
             InjectorUtils.providePaymentViewModelFactory(application,requireContext())
         }*/
        val application = requireNotNull(this).application
        val viewModelFactory = PaymentViewModelFactory(application,sharedPreferences)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PaymentViewModel::class.java)

        startPayment()

        viewModel.message.observe(this, Observer {
            it?.let {
                Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.status.observe(this, Observer {
            when (it) {
                ApiStatus.LOADING -> {
                    loader.setLoading(true)
                }
                ApiStatus.ERROR -> {
                    loader.setLoading(false)
                }
                ApiStatus.DONE -> {
                    loader.setLoading(false)
                }
            }
        })

        viewModel.navigateActivity.observe(this, Observer {
            it?.let {
                if (it == 1) {

                    val intent = Intent(this,HomeActivity::class.java);
                    startActivity(intent)
                    finish();

                }
            }
        })
    }

    fun startPayment() {
        val activity: Activity = this
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", name.toString())
            options.put("description", "MarketLens")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
        //    options.put("amount", finalAmount.toString())
         //   options.put("amount", "12")
            val preFill = JSONObject()
            preFill.put("email", email.toString())
            preFill.put("contact", mobileno.toString())
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    /*override fun onPaymentSuccess(razorpayPaymentID: String) {
        try {
            Log.e(TAG, "Payment Successful: $razorpayPaymentID")
            viewModel.getPaymentid(razorpayPaymentID)

        } catch (e: Exception) {
            Log.e(TAG, "Payment UnSuccessful: " + e.message.toString())
            Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentError(i: Int, s: String) {
        Log.e(
            TAG,
            "Exception in onPaymentSuccess....$i....sss.....$s"
        )
    }*/

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.e(TAG,"Exception in onPaymentSuccess....$p0....sss.....$p1")
        Log.e(TAG,"Exception in onPaymentSuccess....$p2")
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        try {
            Log.e(TAG, "Payment Successful: ${p0.toString()}")
         //   Log.e(TAG, "Payment Successful: ${p1!!.data.toString()}")
        //    Log.e(TAG, "Payment Successful: ${p1!!.orderId.toString()}")
            viewModel.getPaymentid(p0.toString())

        } catch (e: Exception) {
            Log.e(TAG, "Payment UnSuccessful: " + e.message.toString())
            Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
    }
}