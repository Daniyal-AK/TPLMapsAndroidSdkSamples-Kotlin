package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityMapFeaturesBinding
import com.tplmaps3d.MapController
import com.tplmaps3d.MapMode
import com.tplmaps3d.MapView

class ActivityMapFeatures : AppCompatActivity(), MapView.OnMapReadyCallback,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var mMapView: MapView
    private var mMapController: MapController? = null

    private val ENABLE_NIGHT_MODE_DEFAULT: Boolean = false
    private val ENABLE_BUILDINGS_DEFAULT: Boolean = true
    private val ENABLE_POIS_DEFAULT: Boolean = true
    private val ENABLE_TRAFFIC_DEFAULT: Boolean = false
    private lateinit var binding: ActivityMapFeaturesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMapFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)

        val cbNightMode = binding.cbNightMode
        cbNightMode.setOnCheckedChangeListener(this)
        cbNightMode.isChecked = ENABLE_NIGHT_MODE_DEFAULT
        // Setting Night Mode as default
        onCheckedChanged(cbNightMode, ENABLE_NIGHT_MODE_DEFAULT)

        val cbBuildings = binding.cbBuildings
        cbBuildings.setOnCheckedChangeListener(this)
        cbBuildings.isChecked = ENABLE_BUILDINGS_DEFAULT
        // Setting Buildings as default
        onCheckedChanged(cbBuildings, ENABLE_BUILDINGS_DEFAULT)

        val cbPOIs = binding.cbPois
        cbPOIs.setOnCheckedChangeListener(this)
        cbPOIs.isChecked = ENABLE_POIS_DEFAULT
        // Setting POIs as default
        onCheckedChanged(cbPOIs, ENABLE_POIS_DEFAULT)

        val cbTraffic = binding.cbTraffic
        cbTraffic.setOnCheckedChangeListener(this)
        cbTraffic.isChecked = ENABLE_TRAFFIC_DEFAULT
        // Setting POIs as default
        onCheckedChanged(cbTraffic, ENABLE_TRAFFIC_DEFAULT)

        // OR you can make settings for map defaults by calling these methods before call to load maps
        /*mMapView.setMapMode(MapMode.DEFAULT);
        mMapView.setBuildingsEnabled(true);
        mMapView.setPOIsEnabled(true);*/
    }

    override fun onMapReady(mapController: MapController) {
        // TODO: Map loaded and ready, write your map tasks here
        mMapController = mapController

        // TODO: Do your map tasks here

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

    override fun onCheckedChanged(compoundButton: CompoundButton, checked: Boolean) {
        when (compoundButton.id) {
            R.id.cb_night_mode -> mMapView.mapMode =
                if ((checked)) MapMode.NIGHT else MapMode.DEFAULT

            R.id.cb_buildings -> mMapView.setBuildingsEnabled(checked)
            R.id.cb_pois -> mMapView.isPOIsEnabled = checked
            R.id.cb_traffic -> mMapView.isTrafficEnabled = checked
        }
    }

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
}
