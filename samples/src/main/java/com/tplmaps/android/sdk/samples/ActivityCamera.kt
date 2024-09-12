package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityCameraBinding
import com.tplmaps3d.CameraPosition
import com.tplmaps3d.LngLat
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.sdk.model.Bounds

class ActivityCamera : AppCompatActivity(), MapView.OnMapReadyCallback {

    private lateinit var mMapView: MapView
    private var mMapController: MapController? = null
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)

        binding.button.setOnClickListener { v: View? ->
            if (mMapView.mapController == null) return@setOnClickListener
            // Resetting tilt and rotation if made any, previously
            mMapView.mapController.animateCamera(
                CameraPosition.Builder(mMapView.mapController)
                    .tilt(0f)
                    .rotation(0f)
                    .build(), 0
            )
            val southwest = LngLat(73.035070, 33.637313)
            val northeast = LngLat(73.041247, 33.659408)
            // Zoom camera to bounds of Sector I-10, Islamabad with animation
            mMapView.mapController.setBounds(Bounds(southwest, northeast), 300, 1000)
        }
    }

    override fun onMapReady(mapController: MapController) {
        mMapController = mapController

        // Setting map max tilt value
        mapController.setMaxTilt(85f)

        // Applying animation to map camera
        mapController.animateCamera(
            CameraPosition.Builder(mapController)
                .position(LngLat(73.0684356, 33.6934396))
                .zoom(18.5f)
                .rotation(48.0f)
                .tilt(1f)
                .build(), 2000
        )

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

        // Camera Change (ChangeStart, Change, ChangeEnd) listeners
        mapController.setOnCameraChangeStartedListener {
            Log.i(
                TAG,
                "Camera Change Started"
            )
        }
        mapController.setOnCameraChangeListener {
            Log.i(
                TAG,
                "Camera Changing"
            )
        }
        mapController.setOnCameraChangeEndListener { cameraPosition: CameraPosition ->
            Log.i(
                TAG,
                "Camera Change End: $cameraPosition"
            )
        }
    }

    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
    //                                           @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        if (mMapController != null)
    //            mMapController.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (mMapController != null)
            mMapController.onActivityResult(requestCode, resultCode, data);*/
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

    companion object {
        private val TAG: String = ActivityCamera::class.java.simpleName
    }
}
