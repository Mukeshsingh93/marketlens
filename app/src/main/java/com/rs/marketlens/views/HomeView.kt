package com.rs.marketlens.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.loginView.SliderImageAdapter
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.rs.marketlens.R
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.adapter.*
import com.rs.marketlens.databinding.HomeViewBinding
import com.rs.marketlens.databinding.LoginViewBinding
import com.rs.marketlens.network.HomeSlider
import com.rs.marketlens.network.OurServiceVideos
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.HomeViewModel

class HomeView : Fragment() {
    lateinit var binding: HomeViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var latestvideolinearLayoutManager: LinearLayoutManager
    lateinit var featurevideolinearLayoutManager: LinearLayoutManager
    private lateinit var pagerAdapter: SliderImageAdapter
    private var dotscount: Int = 0
    private val _sliderimg = MutableLiveData<List<OurServiceVideos>>()
    val sliderimg: LiveData<List<OurServiceVideos>>
        get() = _sliderimg
    var sliderimage: MutableList<OurServiceVideos>? =  mutableListOf()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = HomeViewBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        (activity as HomeActivity).showToolpBar()

        val viewModel: HomeViewModel by viewModels {
            InjectorUtils.provideHomeViewModelFactory(application,requireContext())
        }
        sliderimage!!.add(OurServiceVideos("X"))
        sliderimage!!.add(OurServiceVideos("X"))
        sliderimage!!.add(OurServiceVideos("X"))
        //  sliderimage!!.add(PropertiesImages("D"))
        _sliderimg.value =  sliderimage



        viewModel.homeslider.observe(viewLifecycleOwner, Observer {

            pagerAdapter = SliderImageAdapter(childFragmentManager, it as  MutableList<HomeSlider> )
            binding.saleviewpager.adapter = pagerAdapter
            dotscount = pagerAdapter.count
            var dots: Array<ImageView> = Array<ImageView>(dotscount) { index -> ImageView(activity as AppCompatActivity) }
            for (i in 0 until dotscount) {
                dots.set(i, ImageView(activity as AppCompatActivity))
                dots!![i].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.non_active_dot))
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(8, 0, 8, 0)
                binding.saleslider.addView(dots!![i], params)
            }
            dots!![0].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.active_dot))
            binding.saleviewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    Log.e("SCROLPOSSITION","scroo position......"+position)
                }
                override fun onPageSelected(position: Int) {

                    Log.e("SCROLPOSSITION","scroo onPageSelected......"+position)

                    for (i in 0 until dotscount) {
                        dots!![i].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.non_active_dot))
                    }
                    dots!![position].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.active_dot))
                }
                override fun onPageScrollStateChanged(state: Int) {
                    Log.e("SCROLPOSSITION","scroo onPageScrollStateChanged......"+state)
                }
            })

        })



        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        latestvideolinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        featurevideolinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.topservicerecyclerview.setLayoutManager(linearLayoutManager)
        binding.latestvideosrecyclerview.setLayoutManager(latestvideolinearLayoutManager)
      //  binding.featuredvideosrecyclerview.setLayoutManager(featurevideolinearLayoutManager)

        val topserviceAdapter = OurServiceAdapter(OnTopServiceClick() { data, type, position ->

            if(data.image.toString().equals(Constant.ONLINECOURSES))
            {
                val action = HomeViewDirections.actionHomeView2ToOnlineCourses(Constant.ONLINECOURSES)
                NavHostFragment.findNavController(this).navigate(action)
            }
            else if(data.image.toString().equals(Constant.WEEKLYSTRATEGY))
            {
                val action = HomeViewDirections.actionHomeView2ToWeeklyStratgyView()
                NavHostFragment.findNavController(this).navigate(action)
            }
            else if(data.image.toString().equals(Constant.MLCALCULATOR))
            {
                val action = HomeViewDirections.actionHomeView2ToMarketLensCalculater()
                NavHostFragment.findNavController(this).navigate(action)
            }
        })

        val latestvideoAdapter = LatestVideosAdapter(OnLatestVideosClick() { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            Log.e("WEBVIEW","Webview is called......"+data.videourl)

            val action = HomeViewDirections.actionHomeView2ToPlayerActivity(data.videourl!!)
            NavHostFragment.findNavController(this).navigate(action)
       /*     if (data.id.toString().equals("1"))
            {
                val action = HomeViewDirections.actionHomeView2ToWebViewFragment("https://marketlens.in/assets/video/"+data.videourl)
                NavHostFragment.findNavController(this).navigate(action)
            }
            else if(data.id.toString().equals("2"))
            {

            }
            else if(data.id.toString().equals("1"))
            {

            }
*/
        })

        //webviewOpen
        viewModel.latestvideos.observe(viewLifecycleOwner, Observer {
            latestvideoAdapter.submitList(it)
            latestvideoAdapter.notifyDataSetChanged()
        })

        val featuredvideoAdapter = FeaturedAdapter(OnFeaturedClick() { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
        })

        viewModel.featuredvideos.observe(viewLifecycleOwner, Observer {
            featuredvideoAdapter.submitList(it)
            featuredvideoAdapter.notifyDataSetChanged()
        })

        viewModel.ourservice.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.e("ADDTOPRODUCT","offer product is....."+it.size)
            topserviceAdapter.submitList(it)
            topserviceAdapter.notifyDataSetChanged()
        })

        binding.topservicerecyclerview.adapter = topserviceAdapter
        binding.latestvideosrecyclerview.adapter = latestvideoAdapter
    //    binding.featuredvideosrecyclerview.adapter = featuredvideoAdapter


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

        //https://marketlens.in/assets/video/weekly-strtegy-video.mp4

        binding.onlineworkshop.setOnClickListener {
            val action = HomeViewDirections.actionHomeView2ToPlayerActivity("weekly-strtegy-video.mp4")

          //  val action = HomeViewDirections.actionHomeView2ToWebViewFragment("https://marketlens.in/assets/video/weekly-strtegy-video.mp4")
            NavHostFragment.findNavController(this).navigate(action)
        //    (activity as HomeActivity).webviewOpen("https://marketlens.in/assets/video/weekly-strtegy-video.mp4")
        }


        viewModel.navigateActivity.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == 1) {

                }
            }
        })

        binding.samplevideoimg.setOnClickListener {
            val action = HomeViewDirections.actionHomeView2ToPlayerActivity("market-lens-trailer.mp4")
          //  val action = HomeViewDirections.actionHomeView2ToWebViewFragment("https://marketlens.in/assets/video/market-lens-trailer.mp4")
            NavHostFragment.findNavController(this).navigate(action)
        }




        binding.connectnow.setOnClickListener {

            (activity as HomeActivity).webviewOpen("https://www.facebook.com/www.marketlens.in/")
        }

      //  binding.viewModel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}