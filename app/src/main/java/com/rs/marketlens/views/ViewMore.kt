package com.rs.marketlens.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.adapter.OnCoursesClick
import com.rs.marketlens.adapter.OnlineCourseAdapter
import com.rs.marketlens.databinding.ViewallcoursesViewBinding
import com.rs.marketlens.viewModel.LoginViewModel

class ViewMore : Fragment() {
    lateinit var binding: ViewallcoursesViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel
    var userId:String=""
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewallcoursesViewBinding.inflate(inflater)
        (activity as HomeActivity).hideToolpBar()
        val application = requireNotNull(this.activity).application
        /*  val viewModel: OnlineCoursesViewModel by viewModels {
              InjectorUtils.provideOnlineCoursesViewModelFactory(application,requireContext())
          }*/
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.onlinecourserecyclerview.setLayoutManager(linearLayoutManager)

        val onlineAdapter = OnlineCourseAdapter(OnCoursesClick() { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
        })

        binding.onlinecourserecyclerview.adapter = onlineAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}