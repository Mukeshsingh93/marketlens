package com.rs.marketlens.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.databinding.PamentViewBinding
import com.rs.marketlens.util.ProgressDialog
import com.rs.marketlens.viewModel.PaymentViewModel
import com.rs.marketlens.viewModel.PaymentViewModelFactory
import org.json.JSONObject


class PaymentGatWay : Fragment()
{
    private val TAG: String = PaymentGatWay::class.java.getSimpleName()
    lateinit var binding: PamentViewBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: PaymentViewModel
    private lateinit var loader: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PamentViewBinding.inflate(inflater)
        sharedPreferences = (activity as AppCompatActivity).getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
       /* val application = requireNotNull(this.activity).application
         viewModel: PaymentViewModel by viewModels {
            InjectorUtils.providePaymentViewModelFactory(application,requireContext())
        }*/
        val application = requireNotNull(this.activity).application
        val viewModelFactory = PaymentViewModelFactory(application,sharedPreferences)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PaymentViewModel::class.java)
      //  binding.viewModel = viewModel
        startPayment();

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
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

        viewModel.navigateActivity.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == 1) {

                    (activity as HomeActivity).paymentSuccessfull()

                }
            }
        })
        return binding.root;
    }

    fun method()
    {
        Log.e(
            TAG,
            "Exception in onPaymentSuccess....method one one one is called"
        )
    }
 /*   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pament_view)

        Checkout.preload(applicationContext)
        startPayment();
    }*/


    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity = (activity as AppCompatActivity)
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Market Lens")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
       //  options.put("order_id", sharedPreferences.getString(Constant.PACKAGEORDERID,"").toString());
          options.put("signature","razorpay_signature");
            options.put("amount","1200")//pass amount in currency subunits
            val prefill = JSONObject()
            prefill.put("email","info@marketlens.in")
            prefill.put("contact","9876543210")
            options.put("prefill",prefill)
            co.open(activity,options)
        //    Log.e("HELLOPRODUCT", "Payment onPaymentError:.... ")
        }catch (e: Exception){
            Log.e("HELLOPRODUCT", "Payment onPaymentError:.... "+e.message.toString())
            Toast.makeText(activity as AppCompatActivity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()

            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }
}