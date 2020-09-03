package com.rs.marketlens.views

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rs.marketlens.databinding.LoginViewBinding
import com.rs.marketlens.databinding.RegisterViewBinding
import com.rs.marketlens.loginview.LoginViewDirections
import com.rs.marketlens.loginview.LoginViewModel
import com.rs.marketlens.util.InjectorUtils

class RegisterView : Fragment() {
    lateinit var binding: RegisterViewBinding
    private lateinit var loader: ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var packagelinearLayoutManager: LinearLayoutManager
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: LoginViewModel by viewModels {
        InjectorUtils.providePlantListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterViewBinding.inflate(inflater)
        (activity as AppCompatActivity).getSharedPreferences("njdjffdj", Context.MODE_PRIVATE).edit {
            remove("kjdkj")
        }
        //     var view = inflater.inflate(R.layout.login_view, container, false)
        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)

        /*val application = requireNotNull(this.activity).application
        val viewModelFactory = HomeViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyOrderViewModel::class.java)
        binding.viewModel = viewModel*/

        binding.signupbtn.setOnClickListener {

            val action = RegisterViewDirections.actionRegisterViewToVerifyOtp()
            NavHostFragment.findNavController(this).navigate(action)

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // loader = ProgressDialog(view.context)
    }
}