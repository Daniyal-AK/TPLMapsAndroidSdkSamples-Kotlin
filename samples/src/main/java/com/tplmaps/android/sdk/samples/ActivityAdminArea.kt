package com.tplmaps.android.sdk.samples

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tplmaps.android.R
import com.tplmaps.android.databinding.ActivityRequestAdminAreaBinding
import com.tplmaps.sdk.places.LngLat
import com.tplmaps.sdk.places.OnSearchResult
import com.tplmaps.sdk.places.Params
import com.tplmaps.sdk.places.Params.Companion.builder
import com.tplmaps.sdk.places.Place
import com.tplmaps.sdk.places.SearchManager
import com.tplmaps3d.sdk.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ActivityAdminArea : AppCompatActivity(), OnSearchResult {
    private var searchManager: SearchManager? = null
    private var strResults: ArrayList<String>? = null
    private var adapter: ArrayAdapter<String>? = null

    private lateinit var binding: ActivityRequestAdminAreaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRequestAdminAreaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Initialize SearchManager
        searchManager = SearchManager(this)

        //searchManager.setListener(this);
        setViews()

        searchManager!!.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (searchManager != null) {
            searchManager!!.onDestroy()
            searchManager = null
        }
    }

    private fun setViews() {
        // Get search field

        val islamabad = LngLat(33.717864, 73.071648)

        // Get and set Search button
        val etSearch = binding.etSearch
        binding.ivSearch.setOnClickListener(View.OnClickListener {
            // Request for query after initializing SearchManager
            // put your query string with location to get your nearer results first
            val lat = etSearch.text.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].toDouble()
            val lng = etSearch.text.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].toDouble()
            searchManager!!.requestNearbyAdminArea(
                builder()
                    .location(LngLat(lat, lng))
                    .build(), this
            )
        })
        etSearch.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent ->
            // Identifier of the action. This will be either the identifier you supplied,
            // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event.action == KeyEvent.ACTION_DOWN
                        && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                //                        Double lat = Double.valueOf(etSearch.getText().toString().split(",")[0]);
//                        Double lng = Double.valueOf(etSearch.getText().toString().split(",")[1]);

//                        searchManager.requestAreaDetail(Params.builder()
//                                        .query(etSearch.getText().toString())
//                               // .location(new LngLat(lat,lng))
//                                .build(), this);

                return@setOnEditorActionListener true
            }
            false
        }
        // Get and set Cancel button
        binding.ivCancel.setOnClickListener {
            // Cancel all pending requests
            etSearch.text.clear()
            searchManager!!.cancelPendingRequests()
            clearList()
        }
    }



    private fun populateListView(results: ArrayList<Place>?) {
        if (results == null) return

        //
//        tv_detail.setText("DIVISION: "+results.get(0).getDivision()+
//                "\nADDRESS: "+results.get(0).getAddressNearby()+
//                "\nPROVINCE: "+results.get(0).getProvinceNearby()+
//                "\nCITY: "+results.get(0).getCity()+
//                "\nSTREET: "+results.get(0).getStreet()+
//                "\nDISTRICT: "+results.get(0).getDistrict()+
//                "\nTEHSIL: "+results.get(0).getTehsil()+
//                "\nSECTOR: "+results.get(0).getSector()
//
//
//        );
        strResults = ArrayList()
        for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, addressNearby, _, _, _, district, tehsil) in results) {
            strResults!!.add(
                """
    ADD: $addressNearby
    Tehsil: $tehsil
    District: $district
    """.trimIndent()
            )
        }

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_2, android.R.id.text1, strResults!!
        )
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, l: Long ->
                val place = results[i]
                val strLocation = """
                ${place.addressNearby}
                ${place.y},${place.x}
                """.trimIndent()
                Log.i(TAG, strLocation)
            }
    }

    fun clearList() {
        if (strResults != null) strResults!!.clear()
        if (adapter != null && strResults != null) {
            strResults!!.size
        }
        if (adapter != null) adapter!!.notifyDataSetChanged()
    }

    override fun onSearchResult(results: ArrayList<Place>) {
        populateListView(results)
    }

    override fun onSearchResultNotFound(params: Params, requestTimeInMS: Long) {
        CommonUtils.showToast(
            this, "Results not found against query: " + params.query +
                    " at " + getDateFormatFromMilliSeconds("dd-MM-yyyy HH:mm:ss", requestTimeInMS),
            2000, true
        )
    }

    override fun onSearchRequestFailure(e: Exception) {
        //e.printStackTrace();
    }

    override fun onSearchRequestCancel(params: Params, requestTimeInMS: Long) {
        Log.i(
            TAG, "Request cancelled against query: " + params.query +
                    " at " + getDateFormatFromMilliSeconds("dd-MM-yyyy HH:mm:ss", requestTimeInMS)
        )
    }

    override fun onSearchRequestSuspended(
        errorMessage: String,
        params: Params,
        requestTimeInMS: Long
    ) {
        Log.e(TAG, "Request Suspended: $errorMessage")
    }


    private fun getDateFormatFromMilliSeconds(format: String, timeInMS: Long): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(Date(timeInMS))
    }

    companion object {
        private val TAG: String = ActivityAdminArea::class.java.simpleName
    }
}
