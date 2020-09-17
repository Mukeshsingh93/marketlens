package com.rs.marketlens.views

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.rs.marketlens.databinding.LoginViewBinding
import com.rs.marketlens.viewModel.LoginViewModel
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.BackViewModel
import com.rs.marketlens.viewModel.BackViewModelFactory

class LoginView : Fragment() {
    lateinit var binding: LoginViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginViewBinding.inflate(inflater)
      /*  (activity as AppCompatActivity).getSharedPreferences("njdjffdj", Context.MODE_PRIVATE).edit {
            remove("kjdkj")
        }*/
       val application = requireNotNull(this.activity).application
         val viewModel: LoginViewModel by viewModels {
            InjectorUtils.provideLoginViewModelFactory(application,requireContext())
        }
       // viewModel.signinApi()

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
                    Log.e(Constant.APIRESPONSE,"api response it it it is called...."+it.toString())

                    val action = com.rs.marketlens.views.LoginViewDirections.actionRegisterViewToVerifyOtp(binding.mobileno.text.toString())
                    NavHostFragment.findNavController(this).navigate(action)
                    viewModel.complete()
                }
            }
        })

        binding.accepttermcondition.setOnClickListener {
            if(binding.accepttermcondition.isChecked)
            {
                viewModel.setTermCondition(true)
            }
            else
            {
                viewModel.setTermCondition(false)
            }
        }

        binding.viewModel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}