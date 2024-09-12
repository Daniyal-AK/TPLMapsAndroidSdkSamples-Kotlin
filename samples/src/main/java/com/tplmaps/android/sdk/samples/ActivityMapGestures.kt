package com.tplmaps.android.sdk.samples

import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityMapGesturesBinding
import com.tplmaps3d.LngLat
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.TouchInput.DoubleTapResponder
import com.tplmaps3d.TouchInput.LongPressResponder
import com.tplmaps3d.TouchInput.PanResponder
import com.tplmaps3d.TouchInput.RotateResponder
import com.tplmaps3d.sdk.model.PointOfInterest
import java.text.DecimalFormat

class ActivityMapGestures : AppCompatActivity(), MapView.OnMapReadyCallback,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var mMapView: MapView

    private lateinit var binding: ActivityMapGesturesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMapGesturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)
        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)



        val cbDoubleTapZoomGesture = binding.cbDtzl
        cbDoubleTapZoomGesture.isChecked = true
        cbDoubleTapZoomGesture.setOnCheckedChangeListener(this)

        val cbMapAllGestures = binding.cbMag
        cbMapAllGestures.isChecked = true
        cbMapAllGestures.setOnCheckedChangeListener(this)

        val cbClick = binding.cbClick
        cbClick.isChecked = false
        cbClick.setOnCheckedChangeListener(this)

        val cbClickDouble = binding.cbClickDouble
        cbClickDouble.isChecked = false
        cbClickDouble.setOnCheckedChangeListener(this)

        val cbClickLong = binding.cbClickLong
        cbClickLong.isChecked = false
        cbClickLong.setOnCheckedChangeListener(this)

        val cbGesturePan = binding.cbGesturePan
        cbGesturePan.isChecked = false
        cbGesturePan.setOnCheckedChangeListener(this)

        val cbGestureRotate = binding.cbGesturesRotate
        cbGestureRotate.isChecked = false
        cbGestureRotate.setOnCheckedChangeListener(this)

        val cbGestureScale = binding.cbGestureScale
        cbGestureScale.isChecked = false
        cbGestureScale.setOnCheckedChangeListener(this)

        val cbGestureShove = binding.cbGestureShove
        cbGestureShove.isChecked = false
        cbGestureShove.setOnCheckedChangeListener(this)

        val cbClickPOI = binding.cbClickPoi
        cbClickPOI.isChecked = false
        cbClickPOI.setOnCheckedChangeListener(this)
    }

    private var mMapController: MapController? = null

    override fun onMapReady(mapController: MapController) {
        mMapController = mapController

        // TODO: Do your map tasks here
        mapController.setPickRadius(resources.getInteger(R.integer.pick_radius).toFloat())
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

        // TODO: Map loaded and ready, write your map tasks here
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, checked: Boolean) {
        when (compoundButton.id) {
            R.id.cb_dtzl -> if (mMapController != null) {
                // Set enable/disable double tap zoom gesture
                mMapController!!.uiSettings.setDoubleTapZoomInGestureEnabled(checked)
            }

            R.id.cb_mag -> if (mMapController != null) {
                // Set enable/disable all gestures
                mMapController!!.uiSettings.isAllMapGesturesEnabled = checked
            }

            R.id.cb_click -> registerListener(ListenerType.CLICK_SINGLE, checked)
            R.id.cb_click_double -> registerListener(ListenerType.CLICK_DOUBLE, checked)
            R.id.cb_click_long -> registerListener(ListenerType.CLICK_LONG, checked)
            R.id.cb_gesture_pan -> registerListener(ListenerType.PAN, checked)
            R.id.cb_gestures_rotate -> registerListener(ListenerType.ROTATE, checked)
            R.id.cb_gesture_scale -> registerListener(ListenerType.SCALE, checked)
            R.id.cb_gesture_shove -> registerListener(ListenerType.SHOVE, checked)
            R.id.cb_click_poi -> registerListener(ListenerType.POI, checked)
        }
    }


    enum class ListenerType {
        CLICK_SINGLE, CLICK_DOUBLE, CLICK_LONG, PAN, ROTATE, SCALE, SHOVE, POI
    }

    private fun registerListener(listenerType: ListenerType?, register: Boolean) {
        when (listenerType) {
            ListenerType.CLICK_SINGLE -> if (mMapController != null) {
                mMapController!!.setOnMapClickListener(if (register) MapController.OnMapClickListener { lngLat: LngLat ->
                    binding.tvListener.text = getString(R.string.click_map)
                    val text = roundDecimalsUpTo(lngLat.latitude, 4)
                        .toString() + ", " + roundDecimalsUpTo(lngLat.longitude, 4)
                    binding.tvValue.text = text
                } else null)
            }

            ListenerType.CLICK_DOUBLE -> if (mMapController != null) {
                mMapController!!.setOnMapDoubleClickListener(if (register) DoubleTapResponder { x: Float, y: Float ->
                    val lngLat = mMapController!!.screenPositionToLngLat(
                        PointF(x, y)
                    )
                    binding.tvListener.text = getString(R.string.click_double_map)
                    val text = roundDecimalsUpTo(lngLat.latitude, 4)
                        .toString() + ", " + roundDecimalsUpTo(lngLat.longitude, 4)
                    binding.tvValue.text = text
                    false
                } else null)
            }

            ListenerType.CLICK_LONG -> if (mMapController != null) {
                mMapController!!.setOnMapLongClickListener(if (register) LongPressResponder { x: Float, y: Float ->
                    val lngLat = mMapController!!.screenPositionToLngLat(
                        PointF(x, y)
                    )
                    binding.tvListener.text = getString(R.string.click_long_map)
                    val text = roundDecimalsUpTo(lngLat.latitude, 4)
                        .toString() + ", " + roundDecimalsUpTo(lngLat.longitude, 4)
                    binding.tvValue.text = text
                } else null)
            }

            ListenerType.PAN -> if (mMapController != null) {
                mMapController!!.setOnMapPanListener(if (register) object : PanResponder {
                    override fun onPan(
                        startX: Float,
                        startY: Float,
                        endX: Float,
                        endY: Float
                    ): Boolean {
                        binding.tvListener.text = getString(R.string.gesture_pan_map)
                        binding.tvValue.text = ""
                        return false
                    }

                    override fun onFling(
                        posX: Float,
                        posY: Float,
                        velocityX: Float,
                        velocityY: Float
                    ): Boolean {
                        binding.tvListener.text = getString(R.string.gesture_fling_map)
                        binding.tvValue.text = ""
                        return false
                    }
                } else null)
            }

            ListenerType.ROTATE -> if (mMapController != null) {
                mMapController!!.setOnMapRotateListener(if (register) RotateResponder { x: Float, y: Float, rotation: Float ->
                    binding.tvListener.text = getString(R.string.gesture_rotate_map)
                    val valueRotation = "Rotation: " + roundDecimalsUpTo(rotation.toDouble(), 2)
                    binding.tvValue.text = valueRotation
                    false
                } else null)
            }

            ListenerType.SCALE -> {}
            ListenerType.SHOVE -> {}
            ListenerType.POI -> if (mMapController != null) {
                mMapController!!.setOnPoiClickListener(if (register) MapController.OnPoiClickListener { place: PointOfInterest ->
                    val lngLat = mMapController!!.screenPositionToLngLat(
                        PointF(
                            place.lngLat.longitude.toFloat(),
                            place.lngLat.latitude.toFloat()
                        )
                    )
                    binding.tvListener.text = getString(R.string.click_poi)
                    val text = (place.name + ", " + roundDecimalsUpTo(lngLat.latitude, 4)
                            + ", " + roundDecimalsUpTo(lngLat.longitude, 4))
                    binding.tvValue.text = text
                } else null)
            }

            null -> TODO()
        }
    }

    private fun roundDecimalsUpTo(d: Double, digitsAfterDecimalPoint: Int): Double {
        if (digitsAfterDecimalPoint <= 0) return d

        val pattern = StringBuilder("#.")

        var digits = digitsAfterDecimalPoint
        while (digits > 0) {
            pattern.append("#")
            digits -= 1
        }

        val decimalFormat = DecimalFormat(pattern.toString())
        return decimalFormat.format(d).toDouble()
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
