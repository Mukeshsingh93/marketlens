package com.rs.marketlens.views

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.rs.marketlens.R
import com.rs.marketlens.databinding.VideoViewBinding
import com.rs.marketlens.util.InjectorUtils
import com.rs.marketlens.viewModel.LoginViewModel


class VideoView : AppCompatActivity() {
    lateinit var binding: VideoViewBinding
    private lateinit var loader: com.rs.marketlens.util.ProgressDialog
    private lateinit var nowPlayingViewModel: LoginViewModel

    // Current playback position (in milliseconds).
    private var mCurrentPosition = 0

    // Tag for the instance state bundle.
    private val PLAYBACK_TIME = "play_time"
    var videoUrl: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.video_view
        )
        videoUrl=  VideoViewArgs.fromBundle(intent?.extras!!).url!!

        // setContentView(R.layout.video_view)

    }

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VideoViewBinding.inflate(inflater)
        videoUrl=  VideoViewArgs.fromBundle(requireArguments()).url!!

        *//*  (activity as AppCompatActivity).getSharedPreferences("njdjffdj", Context.MODE_PRIVATE).edit {
              remove("kjdkj")
          }*//*

        val application = requireNotNull(this.activity).application
        val viewModel: LoginViewModel by viewModels {
            InjectorUtils.provideLoginViewModelFactory(application,requireContext())
        }
        *//*binding.videoplayer.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4")
        binding.videoplayer.start()*//*
        // Set up the media controller widget and attach it to the video view.
        // Set up the media controller widget and attach it to the video view.

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
        val controller = MediaController(activity as AppCompatActivity)
        controller.setMediaPlayer(binding.videoplayer)
        binding.videoplayer.setMediaController(controller)
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





        return binding.root
    }*/

     override fun onStart() {
        super.onStart()
        // Load the media each time onStart() is called.
        initializePlayer()
    }

     override fun onPause() {
        super.onPause()

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.videoplayer.pause()
        }
    }

     override fun onStop() {
        super.onStop()
        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer()
    }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, binding.videoplayer.getCurrentPosition())
    }

    private fun initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        binding.bufferingTextview.setVisibility(android.widget.VideoView.VISIBLE)

        // Buffer and decode the video sample.
    val videoUri: Uri = getMedia("https://marketlens.in/assets/video/"+videoUrl)!!
     //   val videoUri: Uri = getMedia("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4")!!
        binding.videoplayer.setVideoURI(videoUri)

        // Listener for onPrepared() event (runs after the media is prepared).
        binding.videoplayer.setOnPreparedListener(
            OnPreparedListener { // Hide buffering message.
                binding.bufferingTextview.setVisibility(android.widget.VideoView.INVISIBLE)

                // Restore saved position, if available.
                if (mCurrentPosition > 0) {
                    binding.videoplayer.seekTo(mCurrentPosition)
                } else {
                    // Skipping to 1 shows the first frame of the video.
                    binding.videoplayer.seekTo(1)
                }

                // Start playing!
                binding.videoplayer.start()
            })

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        binding.videoplayer.setOnCompletionListener(
            OnCompletionListener {
                Toast.makeText(
                    this,
                    "Value is called ",
                    Toast.LENGTH_SHORT
                ).show()

                // Return the video position to the start.
                binding.videoplayer.seekTo(0)
            })
    }

    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private fun releasePlayer() {
        binding.videoplayer.stopPlayback()
    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private fun getMedia(mediaName: String): Uri? {
        return if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            Log.e("CHECKDATA","ondata is called....."+mediaName.toString())
            Uri.parse(mediaName)
        } else {

            Log.e(Constant.APIRESPONSE,"uri parse else else else api response it it it is called....")

            // you can also put a video file in raw package and get file from there as shown below
            Uri.parse(
                "android.resource://" + getPackageName() +
                        "/raw/" + mediaName
            )
        }
    }


}