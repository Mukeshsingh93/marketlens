package com.rs.marketlens.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.rs.marketlens.R
import com.rs.marketlens.activity.HomeActivity

class WebViewFragment : Fragment()
{

    var progressBar: ProgressBar? = null
//    internal var url = "http://facebook.com"
     var url = ""
    var webView: WebView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view :View =inflater.inflate(R.layout.webviewfragment,container,false)
        (activity as HomeActivity).hideToolpBar()

        url=  WebViewFragmentArgs.fromBundle(requireArguments()).url!!

        Log.e("WEBVIEW","Webview is called......"+url)
        //  val id = SubCategoryViewArgs.fromBundle(arguments!!).id


        var wv: WebView? = null
        wv = view.findViewById<WebView>(R.id.webviewlayout)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
     //   wv.webViewClient = myWebClient()
        wv.settings.javaScriptEnabled = true
        wv.settings.builtInZoomControls = true
        wv.settings.displayZoomControls = false
        wv.loadUrl(url)

        wv.setWebViewClient(object : WebViewClient() {

//            override fun shouldOverrideUrlLoading(view: WebView?,request: WebResourceRequest?): Boolean {
//                progressBar!!.visibility = View.VISIBLE
//                view!!.loadUrl(url)
//                return true
//            }
            override fun onPageFinished(view: WebView, url: String) {
                progressBar!!.visibility = View.GONE
            }
        })

        return view

    }


}


