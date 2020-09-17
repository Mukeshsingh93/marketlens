package com.rs.marketlens.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.rs.marketlens.R
import com.rs.marketlens.activity.HomeActivity
import com.rs.marketlens.databinding.LoginViewBinding
import com.rs.marketlens.databinding.MarketlensViewBinding
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.LoginViewModel
import com.rs.marketlens.viewModel.MarketCalculatorViewModel
import kotlinx.android.synthetic.main.marketlens_view.*

class MarketLensCalculater : Fragment() {
    lateinit var binding: MarketlensViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var viewModel: MarketCalculatorViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarketlensViewBinding.inflate(inflater)
        /*  (activity as AppCompatActivity).getSharedPreferences("njdjffdj", Context.MODE_PRIVATE).edit {
              remove("kjdkj")
          }*/
        (activity as HomeActivity).hideToolpBar()
        val application = requireNotNull(this.activity).application
        val viewModels: MarketCalculatorViewModel by viewModels {
            InjectorUtils.provideMarketCalculatorViewModelFactory(application,requireContext())
        }

        viewModel=viewModels
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
                    viewModel.complete()
                }
            }
        })

        binding.backIcon.setOnClickListener {
            (activity as HomeActivity).onBackPressed()
        }

        viewModel.mlenscalculator.observe(viewLifecycleOwner, Observer {
            binding.ronetxt.text = it.get(0).r1.toString()
            binding.rtwotxt.text = it.get(0).r2.toString()
            binding.pivottxt.text = it.get(0).pivot.toString()
            binding.sonetxt.text = it.get(0).s1.toString()
            binding.stwotxt.text = it.get(0).s2.toString()
            binding.notetxt.text = it.get(0).notes.toString()
            binding.niftyweeklyleveltxt.text = "Nifty Daily Level"
            binding.seeleveltxt.text = "See Below Daily Levels"
            binding.daily.setBackgroundResource(R.drawable.pinksquare_box)
            binding.daily.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,android.R.color.white))

        })


        binding.daily.setOnClickListener {
            binding.daily.setBackgroundResource(R.drawable.pinksquare_box)
            binding.daily.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,android.R.color.white))
            binding.weekly.setBackgroundResource(R.drawable.graysquare_box)
            binding.weekly.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,R.color.blacktext))
            binding.monthly.setBackgroundResource(R.drawable.graysquare_box)
            binding.monthly.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,R.color.grey))
            reflectdata("Daily")
         //   viewModel.populartTest()
        }
        binding.weekly.setOnClickListener {
            binding.weekly.setBackgroundResource(R.drawable.pinksquare_box)
            binding.weekly.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,android.R.color.white))
            binding.daily.setBackgroundResource(R.drawable.graysquare_box)
            binding.daily.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,R.color.blacktext))
            binding.monthly.setBackgroundResource(R.drawable.graysquare_box)
            binding.monthly.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,R.color.blacktext))
            //viewModel.healthPanel()
            reflectdata("Weekly")

        }
        binding.monthly.setOnClickListener {
            binding.monthly.setBackgroundResource(R.drawable.pinksquare_box)
            binding.monthly.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,android.R.color.white))
            binding.weekly.setBackgroundResource(R.drawable.graysquare_box)
            binding.weekly.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,R.color.blacktext))
            binding.daily.setBackgroundResource(R.drawable.graysquare_box)
            binding.daily.setTextColor(ContextCompat.getColor(activity as AppCompatActivity,R.color.blacktext))
            reflectdata("Monthly")
          //  viewModel.testPanel()
        }



        binding.viewModel=viewModel
        return binding.root
    }

    fun reflectdata(type:String)
    {
        for(data in viewModel.mlenscalculator.value!!.indices)
        {
            if(viewModel.mlenscalculator!!.value!!.get(data).duration.equals(type))
            {
                binding.ronetxt.text = viewModel.mlenscalculator!!.value!!.get(data).r1.toString()
                binding.rtwotxt.text = viewModel.mlenscalculator!!.value!!.get(data).r2.toString()
                binding.pivottxt.text =viewModel.mlenscalculator!!.value!!.get(data).pivot.toString()
                binding.sonetxt.text = viewModel.mlenscalculator!!.value!!.get(data).s1.toString()
                binding.stwotxt.text = viewModel.mlenscalculator!!.value!!.get(data).s2.toString()
                binding.notetxt.text = viewModel.mlenscalculator!!.value!!.get(data).notes.toString()
                if(type.equals("Daily"))
                {
                    binding.niftyweeklyleveltxt.text = "Nifty Daily Level"
                    binding.seeleveltxt.text = "See Below Daily Levels"

                }
                else if(type.equals("Weekly"))
                {
                    binding.niftyweeklyleveltxt.text = "Nifty Weekly Level"
                    binding.seeleveltxt.text = "See Below Weekely Levels"
                }
                else if(type.equals("Monthly"))
                {
                    binding.niftyweeklyleveltxt.text = "Nifty Monthly Level"
                    binding.seeleveltxt.text = "See Below Monthly Levels"

                }
            }
      /*      else if(viewModel.mlenscalculator!!.value!!.get(data).equals(type))
            {
                binding.ronetxt.text = viewModel.mlenscalculator!!.value!!.get(data).r1.toString()
                binding.rtwotxt.text = viewModel.mlenscalculator!!.value!!.get(data).r2.toString()
                binding.pivottxt.text =viewModel.mlenscalculator!!.value!!.get(data).pivot.toString()
                binding.sonetxt.text = viewModel.mlenscalculator!!.value!!.get(data).s1.toString()
                binding.stwotxt.text = viewModel.mlenscalculator!!.value!!.get(data).s2.toString()
            }
            else if(viewModel.mlenscalculator!!.value!!.get(data).equals(type))
            {
                binding.ronetxt.text = viewModel.mlenscalculator!!.value!!.get(data).r1.toString()
                binding.rtwotxt.text = viewModel.mlenscalculator!!.value!!.get(data).r2.toString()
                binding.pivottxt.text =viewModel.mlenscalculator!!.value!!.get(data).pivot.toString()
                binding.sonetxt.text = viewModel.mlenscalculator!!.value!!.get(data).s1.toString()
                binding.stwotxt.text = viewModel.mlenscalculator!!.value!!.get(data).s2.toString()
            }*/
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = com.rs.marketlens.util.ProgressDialog(view.context)
    }
}