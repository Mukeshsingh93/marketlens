package com.rs.marketlens.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myapplication.util.Constant
import com.rs.marketlens.R

@BindingAdapter("image")
fun bindAddlImageUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("JSONARRAY","addlImage addlImage...."+imageUrl)
        Glide.with((view.context)).load("https://marketlens.in/assets/images/banners/"+imageUrl)
            .into(view)

    }

}

@BindingAdapter("serviceImage")
fun bindServiceImageUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("JSONARRAY","addlImage addlImage...."+imageUrl)
        if(imageUrl.toString().equals(Constant.ONLINECOURSES))
        {
            Glide.with((view.context)).load(R.drawable.onlinecourses)
                .into(view)
        }
        else  if(imageUrl.toString().equals(Constant.WEEKLYSTRATEGY))
        {
            Glide.with((view.context)).load(R.drawable.weekelystradegy)
                .into(view)
        }
        else  if(imageUrl.toString().equals(Constant.MLCALCULATOR))
        {
            Glide.with((view.context)).load(R.drawable.mcalculator)
                .into(view)
        }
    }

}

@BindingAdapter("latVidImg")
fun bindLatestImageUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("JSONARRAY","addlImage addlImage...."+imageUrl)
        if(imageUrl.toString().equals("1"))
        {
            Glide.with((view.context)).load(R.drawable.latvidfirst)
                .into(view)
        }
        else  if(imageUrl.toString().equals("2"))
        {
            Glide.with((view.context)).load(R.drawable.latvidseccond)
                .into(view)
        }
        else  if(imageUrl.toString().equals("3"))
        {
            Glide.with((view.context)).load(R.drawable.latvidthird)
                .into(view)
        }
    }
}

@BindingAdapter( "viewVisible")
fun viewVisible(view: View, value: Int) {
    if (value==0) {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}