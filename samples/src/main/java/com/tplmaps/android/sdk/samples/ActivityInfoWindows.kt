package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityInfoWindowsBinding
import com.tplmaps3d.IconFactory
import com.tplmaps3d.LngLat
import com.tplmaps3d.MapController
import com.tplmaps3d.MapController.CustomInfoWindow
import com.tplmaps3d.MapView
import com.tplmaps3d.Marker
import com.tplmaps3d.MarkerOptions

class ActivityInfoWindows : AppCompatActivity(), MapView.OnMapReadyCallback {
    private lateinit var binding: ActivityInfoWindowsBinding
    private lateinit var mMapView: MapView
    private var mMapController: MapController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityInfoWindowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)

        //        mMapController.clearMap();
    }

    override fun onMapReady(mapController: MapController) {
        mMapController = mapController

        // TODO: Do your map tasks here

        // Setting map max tilt value
        mapController.setMaxTilt(85f)

        mMapController!!.setLngLat(LngLat(73.093104, 33.730494))
        mMapController!!.setZoomBy(15f)

        normalInfoWindows()
        customInfoWindows()
        customInfoWindowsMultipleViews()

        mMapController!!.setOnMarkerClickListener { tplMarker: Marker ->
            Log.i(
                TAG,
                "Called: tplMarker tile = " + tplMarker.title
            )
        }

        mMapController!!.setOnInfoWindowClickListener { tplMarker: Marker ->
            Log.i(
                TAG,
                "Called: tplMarker snippet = " + tplMarker.description
            )
        }

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

    private fun normalInfoWindows() {
        val marker1 = mMapController!!.addMarker(
            MarkerOptions()
                .position(LngLat(73.093104, 33.730494))
                .title("marker1")
                .description("This is my spot!").flat(false).order(1)
        )

        marker1.showInfoWindow()

        /*marker1.setPosition(new LngLat(73.090947, 33.730283));
        marker1.setSize(new Point(102, 102));
        marker1.setIcon(IconFactory.fromResource(R.drawable.marker_default));
        marker1.setInfoWindowOffset(new Point(-150, 0));
        marker1.hideInfoWindow();
        marker1.setVisible(false);*/
        val marker2 = mMapController!!.addMarker(
            MarkerOptions()
                .position(LngLat(73.092159, 33.728945))
                .title("marker2")
                .description("This is not my spot!").flat(false).order(0)
                .icon(IconFactory.defaultMarker(IconFactory.YELLOW))
        )

        marker2.showInfoWindow()

        //marker2.setTitle("Titlum");
        //marker2.setDescription("TestSnip");
    }

    private fun customInfoWindows() {
        normalInfoWindows()
        // Set custom info view to all info windows
        mMapController!!.setCustomInfoWindow(object : CustomInfoWindow {
            override fun onInfoWindow(tplMarker: Marker): View? {
                return null
            }

            override fun onInfoWindowContent(tplMarker: Marker): View {
                return prepareInfoView(tplMarker)
            }
        })
    }

    var marker3: Marker? = null
    var marker4: Marker? = null

    private fun customInfoWindowsMultipleViews() {
        // Set different custom views to different info windows

        normalInfoWindows()
        marker3 = mMapController!!.addMarker(
            MarkerOptions()
                .position(LngLat(73.096719, 33.728160)).order(0)
                .icon(IconFactory.defaultMarker(IconFactory.GREEN))
        )

        marker3!!.showInfoWindow()

        /*marker3.setPosition(new LngLat(73.092159, 33.728945));
        marker3.setSize(new Point(102, 102));
        marker3.setIcon(IconFactory.fromResource(R.drawable.marker_default));
        marker3.setInfoWindowOffset(new Point(-150, 0));
        marker3.hideInfoWindow();
        marker3.setVisible(false);*/
        marker4 = mMapController!!.addMarker(
            MarkerOptions()
                .position(LngLat(73.090947, 33.730283)).order(0)
                .icon(IconFactory.defaultMarker(IconFactory.RED))
        )

        marker4!!.showInfoWindow()
        marker4!!.setInfoWindowOffset(Point(-150, -20))

        mMapController!!.setCustomInfoWindow(object : CustomInfoWindow {
            override fun onInfoWindow(tplMarker: Marker): View? {
                if (tplMarker === marker3) return prepareInfoView(tplMarker)
                return null
            }

            override fun onInfoWindowContent(tplMarker: Marker): View? {
                if (tplMarker === marker4) return prepareInfoView(tplMarker)
                return null
            }
        })
    }


    private fun prepareInfoView(tplMarker: Marker): View {
        //prepare InfoView programmatically
        val infoView = LinearLayout(this@ActivityInfoWindows)
        val infoViewParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        infoView.orientation = LinearLayout.HORIZONTAL
        infoView.layoutParams = infoViewParams

        val infoImageView = ImageView(this@ActivityInfoWindows)
        val drawable = ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_map)
        infoImageView.setImageDrawable(drawable)
        infoView.addView(infoImageView)

        val subInfoView = LinearLayout(this@ActivityInfoWindows)
        val subInfoViewParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        subInfoView.orientation = LinearLayout.VERTICAL
        subInfoView.layoutParams = subInfoViewParams

        val subInfoLat = TextView(this@ActivityInfoWindows)
        var text = "Lat: " + tplMarker.position.latitude
        subInfoLat.text = text
        val subInfoLnt = TextView(this@ActivityInfoWindows)
        text = "Lnt: " + tplMarker.position.longitude
        subInfoLnt.text = text
        subInfoView.addView(subInfoLat)
        subInfoView.addView(subInfoLnt)
        infoView.addView(subInfoView)

        return infoView
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
        private val TAG: String = ActivityMaps::class.java.simpleName
    }
}
