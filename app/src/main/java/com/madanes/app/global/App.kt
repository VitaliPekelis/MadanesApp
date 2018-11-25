package com.madanes.app.global

import android.app.Application

class App:Application() {

    val sharedPrefs:SharedPrefUtil by lazy {SharedPrefUtil.getInstance(applicationContext)}


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

}