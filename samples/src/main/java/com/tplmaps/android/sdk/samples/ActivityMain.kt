package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.databinding.ActivityMainBinding
import com.tplmaps.android.sdk.samples.ActivityAdminArea
import com.tplmaps.android.sdk.samples.ActivityCamera
import com.tplmaps.android.sdk.samples.ActivityInfoWindows

class ActivityMain : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }
    private fun initViews() {

        binding.btnMaps.setOnClickListener{
            startActivity(Intent(
                this@ActivityMain,
                ActivityMaps::class.java
            )
            )
        }

        binding.btnMapFeatures.setOnClickListener {
            startActivity(
                Intent(
                this@ActivityMain,
                ActivityMapFeatures::class.java
                )
            )
        }

        binding.btnUiControls.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityUIControls::class.java
                )
            )
        }

        binding.btnMapCamera.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityCamera::class.java
                )
            )
        }

        binding.btnMapGestures.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityMapGestures::class.java
                )
            )
        }

        binding.btnSearch.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivitySearch::class.java
                )
            )
        }

        binding.btnRouting.setOnClickListener{
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityRouting::class.java
                )
            )
        }

        binding.btnShapes.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityShapes::class.java
                )
            )
        }

        binding.btnInfoWindows.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityInfoWindows::class.java
                )
            )
        }

        binding.btnMapStyle.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityAdminArea::class.java
                )
            )
        }

        binding.btnAdminArea.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityAdminArea::class.java
                )
            )
        }

        binding.btnLocateMe.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityMain,
                    ActivityLocateMeDemo::class.java
                )
            )
        }
    }

}
