package com.rs.marketlens.views

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.adapter.OnCoursesClick
import com.rs.marketlens.adapter.OnlineCourseAdapter
import com.rs.marketlens.databinding.OnlinecourseViewBinding
import com.rs.marketlens.databinding.SlidercourseViewBinding
import com.rs.marketlens.network.OurServiceVideos
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.LoginViewModel
import com.rs.marketlens.viewModel.OnlineCoursesViewModel

class SliderCourses : Fragment() {
    lateinit var binding: SlidercourseViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel
    var userId:String=""
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var viewModels: OnlineCoursesViewModel
    lateinit var type: String



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        type=  OnlineCoursesArgs.fromBundle(requireArguments()).type!!
        binding = SlidercourseViewBinding.inflate(inflater)
        (activity as HomeActivity).hideToolpBar()

        val application = requireNotNull(this.activity).application
        val viewModel: OnlineCoursesViewModel by viewModels {
            InjectorUtils.provideOnlineCoursesViewModelFactory(application,requireContext())
        }

       // binding.viewModel=viewModel

        viewModels = viewModel
        if(type.equals(Constant.ONLINECOURSES))
        {
            binding.toolbartext.text= Constant.ONLINECOURSES
            viewModel.getLatestVideoApi("online-courses",1)
        }
        else if(type.equals(Constant.WEEKLYSTRATEGY))
        {
            binding.toolbartext.text= Constant.WEEKLYSTRATEGY
              viewModel.getLatestVideoApi("weekly-courses",1)
        }
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.onlinecourserecyclerview.setLayoutManager(linearLayoutManager)
        val onlineAdapter = OnlineCourseAdapter(OnCoursesClick() { data, type, position ->

            if(data.is_paid.toString().equals("1"))
            {
                val action = OnlineCoursesDirections.actionOnlineCoursesToPlayerActivity(data.video_url!!)
                NavHostFragment.findNavController(this).navigate(action)
            }
        })

        viewModel.sliderimg.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.e("ADDTOPRODUCT","offer product is....."+it.size)
            onlineAdapter.submitList(it)
            onlineAdapter.notifyDataSetChanged()
        })
        binding.onlinecourserecyclerview.adapter = onlineAdapter

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
                    val action = OnlineCoursesDirections.actionOnlineCoursesToFinalPaymentGateways2()
                    NavHostFragment.findNavController(this).navigate(action)
                    viewModel.complete()
                }
            }
        })

        viewModel.viewVisible.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == 1) {
                    //     binding.toplayoutview.visibility = View.VISIBLE
                }
            }
        })

        binding.backIcon.setOnClickListener {
            (activity as HomeActivity).onBackPressed()
        }


        viewModel.notify.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == 1) {
                 binding.novideofound.setText(Html.fromHtml(viewModel.packageDetail.value!!.description));
                }
            }
        })


        return binding.root
    }


    fun makeTextViewResizable(
        tv: TextView,
        maxLine: Int,
        expandText: String,
        viewMore: Boolean
    ) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text: String
                val lineEndIndex: Int
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    lineEndIndex = tv.layout.getLineEnd(0)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else {
                    lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                }
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                        viewMore
                    ), TextView.BufferType.SPANNABLE
                )
            }
        })
    }



    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    viewMore()
                    /*  tv.layoutParams = tv.layoutParams
                      tv.setText(tv.tag.toString(), BufferType.SPANNABLE)
                      tv.invalidate()
                      if (viewMore) {
                          viewMore()
                          //makeTextViewResizable(tv, -1, "Read Less", false)
                      } else {
                          viewMore()
                         // viewModel.complete()

                         // makeTextViewResizable(tv, 4, "Read More", true)
                      }*/
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    fun viewMore()
    {
        //   var packagedetail : LiveData<PackageDetails?> = viewModels.packageDetail
        //   val action = OnlineCoursesDirections.actionOnlineCoursesToViewMore(packagedetail)

        //   NavHostFragment.findNavController(this).navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}