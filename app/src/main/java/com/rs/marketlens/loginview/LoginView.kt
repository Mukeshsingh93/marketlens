package com.rs.marketlens.loginview

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rs.marketlens.databinding.LoginViewBinding
import com.rs.marketlens.util.InjectorUtils

class LoginView : Fragment() {
    lateinit var binding: LoginViewBinding
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
        binding = LoginViewBinding.inflate(inflater)
        (activity as AppCompatActivity).getSharedPreferences("njdjffdj", Context.MODE_PRIVATE).edit {
            remove("kjdkj")
        }
        //     var view = inflater.inflate(R.layout.login_view, container, false)
        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)

       binding.signup.setOnClickListener {

           val action = LoginViewDirections.actionLoginViewToRegisterView()
           NavHostFragment.findNavController(this).navigate(action)

       }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // loader = ProgressDialog(view.context)
    }
}