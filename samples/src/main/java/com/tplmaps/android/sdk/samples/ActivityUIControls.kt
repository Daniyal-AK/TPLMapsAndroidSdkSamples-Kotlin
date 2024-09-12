package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityUiControlsBinding
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView

class ActivityUIControls : AppCompatActivity(), MapView.OnMapReadyCallback,
    CompoundButton.OnCheckedChangeListener {
    private var mMapController: MapController? = null
    private lateinit var mMapView: MapView
    private lateinit var binding: ActivityUiControlsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUiControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)
    }

    override fun onMapReady(mapController: MapController) {
        mMapController = mapController

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

        // Setting controls here because functionality of these controls belongs to the MapView
        // And MapView should be ready to perform these actions on it
        (findViewById<View>(R.id.cb_compass) as CheckBox).setOnCheckedChangeListener(
            this
        )

        (findViewById<View>(R.id.cb_zoom_controls) as CheckBox).setOnCheckedChangeListener(
            this
        )
        (findViewById<View>(R.id.cb_my_location_updates) as CheckBox).setOnCheckedChangeListener(
            this
        )
        (findViewById<View>(R.id.cb_my_location_button) as CheckBox).setOnCheckedChangeListener(
            this
        )

        mapController.setOnMyLocationChangeListener(object :
            MapController.OnMyLocationChangeListener {
            override fun onMyLocationChanged(location: Location) {
                Log.d("App", "onMyLocationChanged $location")
            }

            override fun onMyLocationFirstFix(location: Location) {
                Log.d("Location", "onMyLocationFirstFix $location")
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (mMapController != null)
            mMapController.onActivityResult(requestCode, resultCode, data);*/
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, isChecked: Boolean) {
        when (compoundButton.id) {
            R.id.cb_compass ->                 // Show compass
                if (mMapController != null) mMapController!!.uiSettings.showCompass(isChecked)

            R.id.cb_zoom_controls ->                 // Show zoom controls
                if (mMapController != null) mMapController!!.uiSettings.showZoomControls(isChecked)

            R.id.cb_my_location_updates -> try {
                if (mMapController != null) {
                    // Enable/Disable My Location
                    mMapController!!.setMyLocationEnabled(
                        isChecked,
                        MapController.MyLocationArg.ZOOM_LOCATION_ON_FIRST_FIX
                    )
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }

            R.id.cb_my_location_button -> try {
                // Show My Location Button
                if (mMapController != null) mMapController!!.uiSettings.showMyLocationButton(
                    isChecked
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
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
