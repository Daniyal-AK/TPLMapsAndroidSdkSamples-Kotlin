package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityMapStyleBinding
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView

class ActivityMapStyle : AppCompatActivity(), MapView.OnMapReadyCallback {
    private lateinit var mMapView: MapView
    private lateinit var mMapController: MapController
    private lateinit var binding:ActivityMapStyleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMapStyleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Setting custom map style before map ready (pre-execution), map style will be
        // applied on first load/rendering
        //setMapStyle(mMapView);
        // Loading MapView asynchronously via registering callback
        mMapView.loadMapAsync(this)
    }

    override fun onMapReady(mapController: MapController) {
        mMapController = mapController

        // TODO: Do you map tasks here

        // Setting custom map style after map ready (post-call)
        setMapStyle(mMapView)

        // Setting map max tilt value
        mapController.setMaxTilt(85f)
        // Settings map location permission and setting related configuration
        mapController.locationConfig
            .setLocationSettings(true)
            .setPermissionRequestIfDenied(true)
            .setPermissionReasonDialogContent(
                getString(R.string.dialog_reason_title),
                getString(R.string.dialog_reason_message)
            )
        // Loading Default Map UI Controls
        mapController.uiSettings.showZoomControls(true)
        mapController.uiSettings.showMyLocationButton(true)
    }

    private fun setMapStyle(map: MapView?) {
        // Set style specified in a resource file
        map!!.setMapStyle(R.raw.sample_map_style1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}
