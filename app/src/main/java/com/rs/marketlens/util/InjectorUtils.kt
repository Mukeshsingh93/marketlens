package com.rs.marketlens.util

import android.app.Application
import android.content.Context
import com.rs.marketlens.viewModel.*

object InjectorUtils {
   // lateinit var sharedPreferences: SharedPreferences

    private fun getGardenPlantingRepository(context: Context): Repository {
        return Repository.getInstance(context)
    }

    fun provideLoginViewModelFactory(application: Application, fragment: Context): LoginViewModel.LoginViewModelFactory {
        return LoginViewModel.LoginViewModelFactory(application,getGardenPlantingRepository(fragment))
    }

    fun provideVerifyViewModelFactory(application: Application, fragment: Context): VerifyViewModel.VerifyOtpModelFactory {
        return VerifyViewModel.VerifyOtpModelFactory(application,getGardenPlantingRepository(fragment))
    }

    fun provideRegisterViewModelFactory(application: Application, fragment: Context): RegisterViewModel.RegisterViewModelFactory {
        return RegisterViewModel.RegisterViewModelFactory(application,getGardenPlantingRepository(fragment))
    }

    fun provideHomeViewModelFactory(application: Application, fragment: Context): HomeViewModel.HomeViewModelFactory {
        return HomeViewModel.HomeViewModelFactory(application,getGardenPlantingRepository(fragment))
    }

    fun provideOnlineCoursesViewModelFactory(application: Application, fragment: Context): OnlineCoursesViewModel.OnlineCoursesViewModelFactory {
        return OnlineCoursesViewModel.OnlineCoursesViewModelFactory(application,getGardenPlantingRepository(fragment))
    }

    fun provideMarketCalculatorViewModelFactory(application: Application, fragment: Context): MarketCalculatorViewModel.MarketCalculatorViewModelFactory {
        return MarketCalculatorViewModel.MarketCalculatorViewModelFactory(application,getGardenPlantingRepository(fragment))
    }

    /*fun providePaymentViewModelFactory(application: Application, fragment: Context): PaymentViewModel.PaymentViewModelFactory {
        return PaymentViewModel.PaymentViewModelFactory(application,getGardenPlantingRepository(fragment))
    }*/

}