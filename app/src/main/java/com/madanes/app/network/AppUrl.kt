package com.madanes.app.network

import android.net.Uri

object AppUrl {
    private const val schema = "https://"
    private const val host = "8f7lk7qlja.execute-api.eu-west-1.amazonaws.com"
    private const val path = "/dev/"



    fun getBaseUrl(): String
    {
        return "$schema$host$path"
    }
}