package com.rs.marketlens.views

import android.os.Bundle
import android.text.*
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
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.activity.LoginActivity
import com.rs.marketlens.adapter.OnCoursesClick
import com.rs.marketlens.adapter.OnlineCourseAdapter
import com.rs.marketlens.databinding.OnlinecourseViewBinding
import com.rs.marketlens.databinding.VerifyotpViewBinding
import com.rs.marketlens.databinding.ViewallcoursesViewBinding
import com.rs.marketlens.network.OurServiceVideos
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.util.ProgressDialog
import com.rs.marketlens.viewModel.LoginViewModel
import com.rs.marketlens.viewModel.OnlineCoursesViewModel
import com.rs.marketlens.viewModel.VerifyViewModel

class ViewAllCourses : Fragment() {
    lateinit var binding: ViewallcoursesViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel
    var userId:String=""
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var  list:List<OurServiceVideos>;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewallcoursesViewBinding.inflate(inflater)
        (activity as HomeActivity).hideToolpBar()
        list = ViewAllCoursesArgs.fromBundle(requireArguments()).data!!.toList()
        Log.e("MAINDATA","list size is....."+list.size)
        val application = requireNotNull(this.activity).application
      /*  val viewModel: OnlineCoursesViewModel by viewModels {
            InjectorUtils.provideOnlineCoursesViewModelFactory(application,requireContext())
        }*/
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.onlinecourserecyclerview.setLayoutManager(linearLayoutManager)

        val onlineAdapter = OnlineCourseAdapter(OnCoursesClick() { data, type, position ->

            if(data.is_paid.toString().equals("1"))
            {
                val action = ViewAllCoursesDirections.actionViewAllCoursesToPlayerActivity(data.video_url!!)
                NavHostFragment.findNavController(this).navigate(action)
            }

        })

        binding.backIcon.setOnClickListener {
            (activity as HomeActivity).onBackPressed()
        }

        onlineAdapter.submitList(list)
        onlineAdapter.notifyDataSetChanged()
        binding.onlinecourserecyclerview.adapter = onlineAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}