package com.tplmaps.android.sdk.samples

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @get:GET("todos/1")
    val endpointData: Call<JsonObject?>?
}
