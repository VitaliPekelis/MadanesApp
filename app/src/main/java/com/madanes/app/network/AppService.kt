package com.madanes.app.network

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit

interface AppService {

    @Headers("x-api-key: CCTcik4tdF8P2k6QlmnUi74JfjWKcziS3XgW1D7f")
    @GET("common/areacodes")
    fun getMobilePhonePrefix(): Call<MutableList<String?>?>

    companion object {
        fun create():AppService
        {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20000, TimeUnit.MILLISECONDS)
                .addInterceptor(LogJsonInterceptor())
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppUrl.getBaseUrl())
                    .build()

            return retrofit.create(AppService::class.java)
        }
    }
}