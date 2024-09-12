package com.tplmaps.android.sdk.samples

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

object ApiClient {

    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    var retrofit: Retrofit? = null
    var isClosed = false

    @SuppressLint("NewApi")
    @Synchronized
    fun getInstance(): ApiInterface {
        if (retrofit == null) {
            val gson: Gson = GsonBuilder()
                .setLenient()
                .serializeNulls()
                .setPrettyPrinting()
                .create()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .validateEagerly(true)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getRequestHeader())
                .build()
        }
        return retrofit!!.create(ApiInterface::class.java)
    }

    private fun getRequestHeader(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val dispatcher = Dispatcher().apply {
            maxRequests = 25
            maxRequestsPerHost = 20
        }

        return OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request?
                    var response: Response? = null
                    var responseOK = false
                    var tryCount = 0

                    synchronized(this) {
                        while (!responseOK && tryCount < 5) {
                            try {
                                request = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", "")
                                    .build()

                                response = chain.proceed(request!!)
                                if (response!!.isSuccessful || response!!.code == 400 || response!!.code == 404) {
                                    responseOK = true
                                }
                                if (!responseOK && response!!.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                    request = chain.request()
                                        .newBuilder()
                                        .addHeader("Authorization", "")
                                        .build()
                                    response = chain.proceed(request!!)
                                    if (response!!.isSuccessful || response!!.code == 400 || response!!.code == 404) {
                                        responseOK = true
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("intercept", "Request not successful - $tryCount", e)
                            } finally {
                                tryCount++
                            }
                        }
                    }

                    return response ?: Response.Builder()
                        .code(500)
                        .message("retry")
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create("application/json".toMediaTypeOrNull(), "retry"))
                        .addHeader("content-type", "application/json")
                        .build()
                }
            })
            .dispatcher(dispatcher)
            .addInterceptor(interceptor)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }
}
