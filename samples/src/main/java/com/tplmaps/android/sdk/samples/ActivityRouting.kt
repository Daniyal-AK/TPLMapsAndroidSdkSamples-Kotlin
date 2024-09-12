package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tpl.maps.sdk.routing.LngLat
import com.tpl.maps.sdk.routing.StringUtils
import com.tpl.maps.sdk.routing.TPLRouteConfig
import com.tpl.maps.sdk.routing.TPLRouteManager
import com.tpl.maps.sdk.routing.structures.Place
import com.tpl.maps.sdk.routing.structures.TPLRoute
import com.tpl.maps.sdk.utils.boundingBox.RouteUtils
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityRoutingBinding
import com.tplmaps3d.IconFactory
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.MarkerOptions
import com.tplmaps3d.PolylineOptions
import java.util.Objects

class ActivityRouting : AppCompatActivity(), MapView.OnMapReadyCallback {
    private var mRouteManager: TPLRouteManager? = null

    private lateinit var mMapView: MapView
    private var mMapController: MapController? = null
    private lateinit var binding: ActivityRoutingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initiating TPLRouteManager instance
        mRouteManager = TPLRouteManager()
        mRouteManager!!.onCreate(this)

        // Initiating bottom sheet and setting its default state
        initBottomSheet()

        findViewById<View>(R.id.calculate).setOnClickListener { view: View? ->
            val source = (findViewById<View>(R.id.source) as EditText).text.toString()
            val destination = (findViewById<View>(R.id.destination) as EditText).text.toString()
            calculateRoute(mMapController, source, destination)
        }

        // Filled field with sample location values
        val strSrc = "33.711556,73.058382"
        (findViewById<View>(R.id.source) as EditText).setText(strSrc)
        val strDest = "33.522695,73.094223"
        (findViewById<View>(R.id.destination) as EditText).setText(strDest)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)
    }

    private fun initBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
        findViewById<View>(R.id.header_arrow).setOnClickListener { view: View? ->
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                updateUIForCollapsedState()
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                updateUIForExpandedState()
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    (findViewById<View>(R.id.header_arrow) as ImageView).setImageResource(R.drawable.ic_arrow_drop_down)
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    (findViewById<View>(R.id.header_arrow) as ImageView).setImageResource(R.drawable.ic_arrow_drop_up)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        updateUIForExpandedState()
    }

    private fun updateUIForCollapsedState() {
        (findViewById<View>(R.id.header_arrow) as ImageView).setImageResource(R.drawable.ic_arrow_drop_up)
    }

    private fun updateUIForExpandedState() {
        (findViewById<View>(R.id.header_arrow) as ImageView).setImageResource(R.drawable.ic_arrow_drop_down)
    }


    override fun onMapReady(mapController: MapController) {
        mMapController = mapController

        // TODO: Do you map tasks here

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

        findViewById<View>(R.id.calculate).setOnClickListener {
            val source = (findViewById<View>(R.id.source) as EditText).text.toString()
            val destination = (findViewById<View>(R.id.destination) as EditText).text.toString()
            calculateRoute(mapController, source, destination)
        }

        findViewById<View>(R.id.clear).setOnClickListener {
            (findViewById<View>(R.id.source) as EditText).text.clear()
            (findViewById<View>(R.id.destination) as EditText).text.clear()
            mapController.clearMap()
        }
    }

    fun calculateRoute(mapController: MapController?, strSource: String, strDestination: String) {
        if (!(StringUtils.isValidString(strSource) && StringUtils.isValidString(strDestination))) {
            Snackbar.make(
                findViewById(R.id.root), getString(R.string.locations_are_invalid),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        val arrSource = strSource.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val arrDestination =
            strDestination.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Initializing/preparing source and destination locations array
        val locations = ArrayList<Place>()
        // Source location
        val source = Place()
        source.name = "Source"
        source.x = arrSource[1].toDouble()
        source.y = arrSource[0].toDouble()
        // Destination Location
        val destination = Place()
        destination.name = "Destination"
        destination.x = arrDestination[1].toDouble()
        destination.y = arrDestination[0].toDouble()

        locations.add(source)
        locations.add(destination)

        // Building route config
        val params = TPLRouteConfig.Builder.Params(source, destination, false, 90)

        // Building route config
        val config = TPLRouteConfig.Builder().params(params).build()

        val listNodes: MutableList<LngLat> = ArrayList()
        // Calling for calculating routes for source and destination locations with config
        mRouteManager!!.calculate(
            this,
            config
        ) { endPoints: ArrayList<Place?>?, routes: ArrayList<TPLRoute> ->
            if (mapController == null) return@calculate
            for (i in routes.indices) {
                // Drawing/Rendering route on map
                val route = routes[i]
                val lnglats = arrayOfNulls<com.tplmaps3d.LngLat>(route.routeNodes.size)
                for (j in route.routeNodes.indices) {
                    val lngLat = route.routeNodes[j]
                    lnglats[j] = com.tplmaps3d.LngLat(lngLat.longitude, lngLat.latitude)
                }
                mapController.addPolyline(
                    PolylineOptions()
                        .add(*lnglats)
                        .color(if ((i == 0)) Color.BLUE else Color.GRAY)
                        .width(3)
                        .order(2)
                        .outlineWidth(2)
                        .clickable(true)
                )

                listNodes.addAll(route.routeNodes)
            }


            // Setting map (regarding route's extent e.g. zoom and position)
            val routeUtils = RouteUtils(
                mMapView.width, mMapView.height,
                mMapView.scrollX, mMapView.scrollY,
                mapController.mapCameraPosition.zoom.toDouble()
            )
            val mapValues = routeUtils.zoomToPointsBoundingBox(listNodes)

            val zoom =mapValues[RouteUtils.KEY_ZOOM_LEVEL]!!.toDouble()
            val zoomEased = mapValues[RouteUtils.KEY_ZOOM_EASED]!!.toInt()
            val lat = mapValues[RouteUtils.KEY_POSITION_LAT]!!.toDouble()
            val lng = mapValues[RouteUtils.KEY_POSITION_LNG]!!.toDouble()
            val positionEased =
                mapValues[RouteUtils.KEY_POSITION_EASED]!!.toInt()

            mapController.setZoomBy(zoom.toFloat(), zoomEased)
            mapController.setLngLat(com.tplmaps3d.LngLat(lng, lat), positionEased)
        }

        val marker = mapController!!.addMarker(
            MarkerOptions()
                .position(
                    com.tplmaps3d.LngLat(73.058382, 33.711556)
                )
                .title("title").description("Description")
        )

        val marker1 = mapController.addMarker(
            MarkerOptions()
                .position(
                    com.tplmaps3d.LngLat(73.094223, 33.522695)
                )
                .title("title").description("Description")
        )

        marker1.icon = IconFactory.fromResource(R.drawable.ic_pin_drop)
        marker.icon = IconFactory.fromResource(R.drawable.current_location_marker)
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

        if (mRouteManager != null) {
            mRouteManager!!.onDestroy()
            mRouteManager = null
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}
