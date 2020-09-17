package com.rs.marketlens.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.rs.marketlens.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PaymentGatWays extends Activity implements PaymentResultWithDataListener {

    String TAG="PAYMENTTAG";
    @Override
    protected void attachBaseContext(Context newBase) {



        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instamojo_payment);
         startPayment();
    }


    public void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Mukesh");
            options.put("description", "BigDealStore");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "1");
            JSONObject preFill = new JSONObject();
            preFill.put("email", "mk@gmail.com");
            preFill.put("contact", "98855555");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

   /* @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Log.e(TAG, "Payment Successful: " + razorpayPaymentID);

        } catch (Exception e) {
            Log.e(TAG, "Payment UnSuccessful: " + e.getMessage().toString());
             Toast.makeText(getApplicationContext(),  e.getMessage().toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "Exception in onPaymentSuccess...."+i+"....sss....."+s.toString());
    }*/


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.e(TAG, "Payment Successful: " + s);

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e(TAG, "Exception in onPaymentSuccess...."+i+"....sss....."+s.toString());

    }
}
