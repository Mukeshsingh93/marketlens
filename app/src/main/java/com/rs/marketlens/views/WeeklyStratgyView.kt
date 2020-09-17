package com.rs.marketlens.views

import android.R
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
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.adapter.MonthlyAdapter
import com.rs.marketlens.adapter.OnCoursesClick
import com.rs.marketlens.adapter.OnlineCourseAdapter
import com.rs.marketlens.databinding.OnlinecourseViewBinding
import com.rs.marketlens.databinding.WeeklystrtgyViewBinding
import com.rs.marketlens.network.MontlyPlan
import com.rs.marketlens.network.OurServiceVideos
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.LoginViewModel
import com.rs.marketlens.viewModel.OnlineCoursesViewModel
import com.rs.marketlens.views.OnlineCoursesDirections.actionOnlineCoursesToPlayerActivity

class WeeklyStratgyView : Fragment() {
    lateinit var binding: WeeklystrtgyViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel
    var userId:String=""
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var viewModels: OnlineCoursesViewModel
    lateinit var type: String
    private lateinit var  subsPlan:List<MontlyPlan>

    lateinit var subid: String
    lateinit var price: String
    lateinit var discountprice: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeeklystrtgyViewBinding.inflate(inflater)
        (activity as HomeActivity).hideToolpBar()

        val application = requireNotNull(this.activity).application
        val viewModel: OnlineCoursesViewModel by viewModels {
            InjectorUtils.provideOnlineCoursesViewModelFactory(application,requireContext())
        }
        binding.viewModel=viewModel

        viewModels = viewModel
        binding.toolbartext.text= Constant.WEEKLYSTRATEGY
        viewModel.getLatestVideoApi("weekly-courses",0)

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.onlinecourserecyclerview.setLayoutManager(linearLayoutManager)
        val onlineAdapter = OnlineCourseAdapter(OnCoursesClick() { data, type, position ->
            if(data.is_paid.toString().equals("1"))
            {
                val action = WeeklyStratgyViewDirections.actionWeeklyStratgyViewToPlayerActivity(data.video_url!!)
                NavHostFragment.findNavController(this).navigate(action)
            }
        })

        viewModel.sliderimg.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.e("ADDTOPRODUCT","offer product is....."+it.size)
            onlineAdapter.submitList(it)
            onlineAdapter.notifyDataSetChanged()
        })


          //mk  viewModel.getOrderIdApi()


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
                    val action = WeeklyStratgyViewDirections.actionWeeklyStratgyViewToFinalPaymentGateways2()
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

        viewModel.notify.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == 1) {
                    binding.viewModel= viewModel
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.courseinfodestxt.setText(Html.fromHtml(viewModel.packageDetail.value!!.description, Html.FROM_HTML_MODE_LEGACY));
                    } else
                        binding.courseinfodestxt.setText(Html.fromHtml(viewModel.packageDetail.value!!.description));
                    makeTextViewResizable(binding.courseinfodestxt, 3, "Read More", true);

                }
            }
        })

        binding.viewall.setOnClickListener {
            val list:List<OurServiceVideos> = viewModel.sliderimage!!
            Log.e("MAINDATA","list before size is....."+list.size)
        /*    val action = OnlineCoursesDirections.actionOnlineCoursesToViewAllCourses(list.toTypedArray())
            NavHostFragment.findNavController(this).navigate(action)
            viewModel.complete()*/
            //  it.findNavController().navigate(R.id.action_onlineCourses_to_viewAllCourses,bundle)
        }

        viewModel.subsPeriod.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //   city=it
            subsPlan=it!!
            val pickerAdapter = MonthlyAdapter((activity as AppCompatActivity),
                R.layout.simple_spinner_item, subsPlan!!)
            binding.durationspinner.setAdapter(pickerAdapter)
        })

        binding.durationspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               // viewModel.setCity( city.get(position).id!!.toString())
                subid=subsPlan.get(position).id!!
                price=subsPlan.get(position).price!!
                discountprice="0"
                binding.totalprice.text="₹ "+subsPlan.get(position).price
                var totalPrice :Int= subsPlan.get(position).price!!.toInt()

                var gst:Double = totalPrice.times(18).div(100).toDouble()
                var finalTotalPrice:Double=totalPrice.plus(gst).toDouble()

                binding.discountprice.text="₹ "+"0"
                var totalamountpaid:Int=subsPlan.get(position).price!!.toInt().plus(0)
                binding.amounttobepaid.text="₹ "+finalTotalPrice
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        binding.subscribe.setOnClickListener {
            viewModel.getWeeklyOrderIdApi(subid,price,discountprice)

        }

        binding.backIcon.setOnClickListener {
            (activity as HomeActivity).onBackPressed()
        }
        binding.viewall.setOnClickListener {
            val list:List<OurServiceVideos> = viewModel.sliderimage!!
            Log.e("MAINDATA","list before size is....."+list.size)
            val action = WeeklyStratgyViewDirections.actionWeeklyStratgyViewToViewAllCourses(list.toTypedArray())
            NavHostFragment.findNavController(this).navigate(action)
            viewModel.complete()
            //  it.findNavController().navigate(R.id.action_onlineCourses_to_viewAllCourses,bundle)
        }
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