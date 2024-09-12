package com.tplmaps.android.sdk.samples

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityMapsBinding
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.sdk.utils.CommonUtils

class ActivityMaps : AppCompatActivity(), MapView.OnMapReadyCallback {

    private lateinit var mMapView: MapView
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)

        /* MapView is initialized here, call enable traffic function now (Traffic is disabled by default)
         * Calling function here means map will be loaded with the traffic update (pre call),
         * you can also call function in onMapReady callback method means traffic update
         * will be applied when map will be on ready to render state.*/
        //mMapView.setTrafficEnabled(true);

        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)
    }

    override fun onMapReady(mapController: MapController) {
        CommonUtils.showToast(this, "Map Ready", Toast.LENGTH_SHORT, false)

        // TODO: Map loaded and ready, perform your map operations from here
        // Setting map max tilt value
        mapController.setMaxTilt(85f)

        /* MapView is initialized here, call enable traffic function now (Traffic is disabled by default)
         * Calling function here means map will update traffic when it is loaded and ready (post call),
         * you can also call function in onMapReady callback method means traffic update
         * will be applied when map will be on ready to render state.*/
        //mMapView.setTrafficEnabled(true);
        mapController.uiSettings.showZoomControls(false)
        mapController.uiSettings.showMyLocationButton(false)


        mapController.setMyLocationEnabled(
            true,
            MapController.MyLocationArg.ZOOM_LOCATION_ON_FIRST_FIX
        )
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
