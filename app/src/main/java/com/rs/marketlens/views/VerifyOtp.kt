package com.rs.marketlens.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.rs.marketlens.databinding.VerifyotpViewBinding
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.util.ProgressDialog
import com.rs.marketlens.viewModel.VerifyViewModel

class VerifyOtp : Fragment(), TextWatcher {
    lateinit var binding: VerifyotpViewBinding
    private lateinit var loader: ProgressDialog
    var mobileno:String=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VerifyotpViewBinding.inflate(inflater)
        mobileno = VerifyOtpArgs.fromBundle(requireArguments()).mobileno!!

    //    binding.senttomsg.text="Sent to ${mobileno.toString()}"

        val application = requireNotNull(this.activity).application
        val viewModel: VerifyViewModel by viewModels {
            InjectorUtils.provideVerifyViewModelFactory(application,requireContext())
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
                    val action = VerifyOtpDirections.actionVerifyOtpToRegister()
                    NavHostFragment.findNavController(this).navigate(action)
                }
                else if(it == 2)
                {
                    (activity as LoginActivity).startHomeActivity()

                }
            }
        })

        binding.submit.setOnClickListener {
            var otp= binding.edittextone.text.toString().plus(binding.edittexttwo.text.toString()).plus(binding.edittextthree.text.toString()).plus(binding.edittextfour.text.toString())
            Log.e("VERIFICATIONVIEW","verficationview....."+otp.toString())
            if(binding.edittextone.text.toString().isEmpty()||binding.edittexttwo.text.toString().isEmpty()||binding.edittextthree.text.toString().isEmpty()||binding.edittextfour.text.toString().isEmpty())
            {
                Toast.makeText(activity,"Otp is not valid", Toast.LENGTH_SHORT).show()
            }
            else
            {
                viewModel.checkOtpApi(otp.toString(),mobileno.toString())
            }
        }
        binding.edittextone.setText("");
        binding.edittexttwo.setText("");
        binding.edittextthree.setText("");
        binding.edittextfour.setText("");
        binding.edittextone.addTextChangedListener(this);
        binding.edittexttwo.addTextChangedListener(this);
        binding.edittextthree.addTextChangedListener(this);
        binding.edittextfour.addTextChangedListener(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }

    override fun afterTextChanged(s: Editable?) {
        if (s!!.length == 1) {
            if (binding.edittextone.length() == 1) {
                binding.edittexttwo.requestFocus();
            }
            if (binding.edittexttwo.length() == 1) {
                binding.edittextthree.requestFocus();
            }
            if (binding.edittextthree.length() == 1) {
                binding.edittextfour.requestFocus();
            }

        }
        else if(s!!.length==0) {

            if (binding.edittextfour.length() == 0) {
                binding.edittextthree.requestFocus();
            }
            if (binding.edittextthree.length() == 0) {
                binding.edittexttwo.requestFocus();
            }
            if (binding.edittexttwo.length() == 0) {
                binding.edittextone.requestFocus();
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}