package com.madanes.app.network

import retrofit2.Call

object NetworkHandler {

    private val appService by lazy {
        AppService.create()
    }

    fun getMobilePhonePrefix() : Call<MutableList<String?>?> {
        return appService.getMobilePhonePrefix()
    }
}