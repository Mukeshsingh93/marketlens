package com.rs.marketlens.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.util.ApiStatus
import com.rs.marketlens.activity.LoginActivity
import com.rs.marketlens.databinding.LoginViewBinding
import com.rs.marketlens.databinding.RegisterViewBinding
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.LoginViewModel
import com.rs.marketlens.viewModel.RegisterViewModel

class Register : Fragment() {
    lateinit var binding: RegisterViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel
    var userId:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterViewBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val viewModel: RegisterViewModel by viewModels {
            InjectorUtils.provideRegisterViewModelFactory(application,requireContext())
        }
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
                    (activity as LoginActivity).startHomeActivity()
                }
            }
        })

        binding.viewModel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}