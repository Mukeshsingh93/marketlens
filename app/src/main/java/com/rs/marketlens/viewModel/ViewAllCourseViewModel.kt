package com.rs.marketlens.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.util.ApiStatus
import com.rs.marketlens.network.OurServiceVideos
import com.rs.marketlens.util.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ViewAllCourseViewModel (application: Application, val repository: Repository
) : AndroidViewModel(application){

    val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope( viewmodelJob+ Dispatchers.Main )
    private val context = getApplication<Application>().applicationContext

    var userid:String=""

    private val _message = MutableLiveData<String>()
    val message: LiveData<String?>
        get() = _message



    private val _navigateActivity = MutableLiveData<Int>()
    val navigateActivity : LiveData<Int?>
        get() = _navigateActivity


    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _sliderimg = MutableLiveData<List<OurServiceVideos>>()
    val sliderimg: LiveData<List<OurServiceVideos>>
        get() = _sliderimg
    var sliderimage: MutableList<OurServiceVideos>? =  mutableListOf()


    init {
        sliderimage!!.add(OurServiceVideos("X"))
        sliderimage!!.add(OurServiceVideos("X"))
        sliderimage!!.add(OurServiceVideos("X"))
        sliderimage!!.add(OurServiceVideos("X"))
        sliderimage!!.add(OurServiceVideos(""))
        sliderimage!!.add(OurServiceVideos("Relapsing Polychondritis"))
        sliderimage!!.add(OurServiceVideos("Celiac Disease"))
        sliderimage!!.add(OurServiceVideos("Allergies & Asthma"))
        sliderimage!!.add(OurServiceVideos("RHEUMATOID ARTHRITIS"))
        //  sliderimage!!.add(PropertiesImages("D"))
        _sliderimg.value =  sliderimage
        _navigateActivity.value=0
    }



    class ViewAllCoursesViewModelFactory(private val application: Application,
                                        val sharedPreferences: Repository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ViewAllCourseViewModel::class.java)) {
                return ViewAllCourseViewModel(
                    application, sharedPreferences
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}