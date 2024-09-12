package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityShapesBinding
import com.tplmaps3d.Circle
import com.tplmaps3d.CircleOptions
import com.tplmaps3d.IconFactory
import com.tplmaps3d.LngLat
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.Marker
import com.tplmaps3d.MarkerOptions
import com.tplmaps3d.Polygon
import com.tplmaps3d.PolygonOptions
import com.tplmaps3d.Polyline
import com.tplmaps3d.PolylineOptions
import com.tplmaps3d.sdk.model.PointOfInterest
import java.util.Objects

class ActivityShapes : AppCompatActivity(), MapView.OnMapReadyCallback {
    private var mMapController: MapController? = null
    private lateinit var mMapView: MapView
    private lateinit var binding: ActivityShapesBinding
    private var marker1: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShapesBinding.inflate(layoutInflater)

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


        mMapController!!.setOnMapClickListener { lngLat: LngLat ->
            Log.i(TAG, "Called: onMapClick lnglat = " + lngLat.longitude + " , " + lngLat.latitude)
        }

        mMapController!!.setOnPoiClickListener { place: PointOfInterest ->
            Log.i(TAG, "Called: onPoiClick id = " + place.id)
            Log.i(TAG, "Called: onPoiClick tile = " + place.name)
            Log.i(
                TAG,
                "Called: onPoiClick lnglat = " + place.lngLat.longitude + " , " + place.lngLat.latitude
            )
            Log.i(TAG, " // /// ///// /// ")
        }

        mMapController!!.setOnMarkerClickListener { tplMarker: Marker ->
            Log.i(TAG, "Called: tplMarker tile = " + tplMarker.title)
            if (!tplMarker.isInfoWindowShown) {
                tplMarker.showInfoWindow()
                Log.i(TAG, "Show Request")

                Toast.makeText(this, "Show Request", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Already Shown")

                Toast.makeText(this, "Already Shown", Toast.LENGTH_SHORT).show()
            }
        }

        mMapController!!.setOnInfoWindowClickListener { tplMarker: Marker ->
            Log.i(
                TAG,
                "Called: tplMarker snippet = " + tplMarker.description
            )
        }

        mMapController!!.setOnPolylineClickListener { tplPolyline: Polyline ->
            Log.i(
                TAG,
                "Called: tplPolyline order = " + tplPolyline.order
            )
        }

        mMapController!!.setOnPolygonClickListener { tplPolygon: Polygon ->
            Log.i(TAG, "Called: tplPolygon stroke width = " + tplPolygon.outlineWidth)
        }

        mMapController!!.setOnCircleClickListener { tplCircle: Circle ->
            Log.i(TAG, "Called: tplCircle stroke radius = " + tplCircle.radius)
            Log.i(
                TAG,
                "Called: total markers = " + Objects.requireNonNull(mapController.allMarkers).size
            )
            Log.i(
                TAG,
                "Called: total polylines = " + Objects.requireNonNull(mapController.allPolyLines).size
            )
            Log.i(
                TAG,
                "Called: total polygons = " + Objects.requireNonNull(mapController.allPolygons).size
            )
            Log.i(
                TAG,
                "Called: total circles = " + Objects.requireNonNull(mapController.allCircles).size
            )
        }

        // Loading Default Map Controls
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


        val handler = Handler()
        handler.postDelayed({
            addMarkers()
            addPolyLines()
            addPolygons()
            addCircles()
        }, 2000)
    }


    private fun addMarkers() {
        val latLngISB = LngLat(73.093104, 33.730494)
        marker1 = mMapController!!.addMarker(
            MarkerOptions()
                .position(latLngISB)
                .title("marker1")
                .description("This is my spot!").infoWindowOffset(Point(-150, 0))
                .rotation(50f).flat(true)
                .icon(IconFactory.fromResource(R.drawable.ic_pin_drop)).visible(false).order(1)
        )

        marker1!!.isFlat = false

        marker1!!.rotation = 0f
        marker1!!.setInfoWindowOffset(Point(0, 0))
        marker1!!.properties = latLngISB
        marker1!!.title = "Titlum"
        marker1!!.description = "aklsdfj"
        marker1!!.isVisible = true
        marker1!!.showInfoWindow()


        val marker2 = mMapController!!.addMarker(
            MarkerOptions()
                .position(LngLat(73.092159, 33.728945))
                .title("marker2")
                .description("This is not my spot!").flat(false).order(0)
        )

        marker2.showInfoWindow()
        marker2.title =
            "ksd lfjaksdl fjklad fjkads fklads fjkalsd fjklad fjkalds fjaksd fjaksdl fjakldf jakld fjkald fjkladsf jaklsd fjkals"

        mMapController!!.removeMarker(marker2)
    }

    private fun addPolyLines() {
        val polyline1 = mMapController!!.addPolyline(
            PolylineOptions()
                .add(LngLat(73.094177, 33.729113), LngLat(73.090913, 33.727616))
                .add(LngLat(73.090913, 33.727616), LngLat(73.096118, 33.728488))
                .add(LngLat(73.096118, 33.728488), LngLat(73.106513, 33.714936))
                .color(Color.WHITE).width(10).order(5).outlineWidth(2)
                .outlineColor(Color.BLUE).clickable(true)
        )
        polyline1.outlineWidth = 5
        polyline1.outlineColor = Color.parseColor("#FF69B4")

        val polyline2 = mMapController!!.addPolyline(
            PolylineOptions()
                .add(
                    LngLat(71.094177, 33.729113),
                    LngLat(73.090913, 33.727616)
                ).color(Color.RED).width(5).order(5)
        )
        polyline2.remove()
    }

    private fun addPolygons() {
        val lngLats1 = ArrayList<LngLat>()
        lngLats1.add(LngLat(73.092159, 33.728945))
        lngLats1.add(LngLat(73.092620, 33.727624))
        lngLats1.add(LngLat(73.091322, 33.726795))
        lngLats1.add(LngLat(73.092159, 33.728945))
        val tplPolygon = mMapController!!.addPolygon(
            PolygonOptions().addAll(lngLats1).order(2)
                .fillColor(Color.BLACK).outlineColor(Color.GRAY).outlineWidth(10)
        )
        tplPolygon.isClickable = true

        val lngLats2 = ArrayList<LngLat>()
        lngLats2.add(LngLat(73.092159, 33.728945))
        lngLats2.add(LngLat(73.093758, 33.728300))
        lngLats2.add(LngLat(73.093190, 33.729430))
        lngLats2.add(LngLat(73.092159, 33.728945))

        mMapController!!.addPolygon(
            PolygonOptions().addAll(lngLats2)
                .order(10).fillColor(0x7F00FF00).outlineColor(Color.GREEN).outlineWidth(10)
                .clickable(true)
        )

        mMapController!!.removePolygon(tplPolygon)
    }

    private fun addCircles() {
        val tplCircle = mMapController!!.addCircle(
            CircleOptions()
                .center(LngLat(73.092159, 33.728945))
                .radius(30.0).fillColor(Color.CYAN)
                .order(1).clickable(true)
        )

        mMapController!!.addCircle(
            CircleOptions()
                .center(LngLat(73.093104, 33.730494))
                .radius(150.0).fillColor(Color.BLUE)
                .order(1).clickable(true)
        )

        mMapController!!.setMaxTilt(85f)
        mMapController!!.setLngLat(LngLat(73.093104, 33.730494))
        mMapController!!.setZoomBy(15f)

        tplCircle.remove()
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

    companion object {
        private val TAG: String = ActivityMaps::class.java.simpleName
    }
}