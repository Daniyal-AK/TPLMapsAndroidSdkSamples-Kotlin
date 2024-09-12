package com.tplmaps.android.sdk.samples

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityLocateMeDemoBinding
import com.tplmaps.sdk.places.LngLat
import com.tplmaps.sdk.places.OnSearchResult
import com.tplmaps.sdk.places.Params
import com.tplmaps.sdk.places.Params.Companion.builder
import com.tplmaps.sdk.places.Place
import com.tplmaps.sdk.places.SearchManager
import com.tplmaps3d.CameraPosition
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.sdk.utils.CommonUtils


class ActivityLocateMeDemo : AppCompatActivity(), MapView.OnMapReadyCallback, OnSearchResult {
    //private static final String TAG = ActivityMaps.class.getSimpleName();
    private lateinit var mMapView: MapView
    private lateinit var searchManager: SearchManager
    private var searchLngLat: LngLat? = null
    private var isSearch: Boolean = false
    private lateinit var mMapController: MapController
    private var strResults: ArrayList<String>? = null
    private var adapter: ArrayAdapter<String>? = null


    private lateinit var binding: ActivityLocateMeDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLocateMeDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Getting MapView resource from layout
        mMapView = findViewById(R.id.map)


        // Initialize SearchManager
        searchManager = SearchManager(this)

        /* MapView is initialized here, call enable traffic function now (Traffic is disabled by default)
         * Calling function here means map will be loaded with the traffic update (pre call),
         * you can also call function in onMapReady callback method means traffic update
         * will be applied when map will be on ready to render state.*/
        //mMapView.setTrafficEnabled(true);

        // Calling MapView's onCreate() lifecycle method
        mMapView.onCreate(savedInstanceState)
        // Loading map Asynchronously vie registering call
        mMapView.loadMapAsync(this)




        // Set up search functionality
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    searchManager.requestOptimizeSearch(
                        Params.builder()
                            .query(binding.etSearch.text.toString())
                            .location(searchLngLat)
                            .build(), this
                    )
                    isSearch = true
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                }
                true
            } else {
                false
            }
        }


        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isEmpty()) {
                    if (binding.listview.visibility == View.VISIBLE) {
                        binding.listview.visibility=View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })


        //Map Zoom-In
        binding.ibZoomIn.setOnClickListener {
            mMapController.setZoomBy(
                mMapController.mapCameraPosition.zoom + 1
            )
        }

        // Map Zoom-Out
        binding.ibZoomOut.setOnClickListener {
            mMapController.setZoomBy(
                mMapController.mapCameraPosition.zoom - 1
            )
        }

        //Map Current Location
        binding.ibLocateMe.setOnClickListener {
            val cameraPosition = CameraPosition(
                mMapController, com.tplmaps3d.LngLat(
                    mMapController.getMyLocation(mMapView).longitude,
                    mMapController.getMyLocation(mMapView).latitude
                ), 14f, 0f, 0f
            )
            mMapController.animateCamera(cameraPosition, 1000)
        }
    }

    override fun onMapReady(mapController: MapController) {
        CommonUtils.showToast(this, "Map Ready", Toast.LENGTH_SHORT, false)


        mapController.removeCurrentLocationMarker()


        mMapController = mapController
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


        mapController.setOnCameraChangeEndListener { cameraPosition: CameraPosition ->
            runOnUiThread {
                searchLngLat =
                    LngLat(cameraPosition.position.longitude, cameraPosition.position.latitude)
                binding.tvAdd.text =
                    String.format("%.4f", cameraPosition.position.latitude) + " ; " + String.format(
                        "%.4f",
                        cameraPosition.position.longitude
                    )
                searchManager.requestReverse(
                    builder()
                        .location(
                            LngLat(
                                cameraPosition.position.latitude,
                                cameraPosition.position.longitude
                            )
                        )
                        .build(), this@ActivityLocateMeDemo
                )
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

    override fun onSearchResult(results: ArrayList<Place>) {
        Log.d("TAG", "onSearchResult: $results")
        if (isSearch) {
            populateListView(results)
            isSearch = false
        } else {
            binding.tvFullAdd.text = results[0].address
        }
    }

    override fun onSearchResultNotFound(params: Params, requestTimeInMS: Long) {
    }

    override fun onSearchRequestFailure(e: Exception) {
    }

    override fun onSearchRequestCancel(params: Params, requestTimeInMS: Long) {
    }

    override fun onSearchRequestSuspended(
        errorMessage: String,
        params: Params,
        requestTimeInMS: Long
    ) {
    }



    private fun populateListView(results: ArrayList<Place>?) {
        if (results == null) return

        strResults = ArrayList()
        for ((name) in results) {
            strResults!!.add(name)
        }

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            strResults!!
        )

        binding.listview.visibility = View.VISIBLE
        binding.listview.adapter = adapter

        binding.listview.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, view: View?, i: Int, l: Long ->
                val place = results[i]
                val strLocation = """
                ${place.name}
                ${place.y},${place.x}
                """.trimIndent()

                binding.listview.visibility = View.GONE

                mMapController.setLngLatZoom(
                    com.tplmaps3d.LngLat(
                        place.x.toDouble(),
                        place.y.toDouble()
                    ), 15.0f
                )

                binding.tvAdd.text = place.y + " ; " + place.x

                binding.tvFullAdd.text = place.address
                clearList()
            }
    }


    private fun clearList() {
        if (strResults != null) strResults!!.clear()
        if (adapter != null && strResults != null) {
            strResults!!.size
        }
        if (adapter != null) adapter!!.notifyDataSetChanged()

        binding.etSearch.setText("")
    }
}