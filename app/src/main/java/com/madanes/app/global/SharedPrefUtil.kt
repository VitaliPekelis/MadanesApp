package com.madanes.app.global

import android.content.Context
import android.content.SharedPreferences
import android.text.format.DateUtils


private const val MOBILE_PHONE_PREFIX = "mobile_phone_prefix"
private const val MOBILE_PHONE_PREFIX_DATE = "mobile_phone_prefix_DATE"

class SharedPrefUtil private constructor() : SharedPrefUtilGlobal(){

    private val monthInMills = DateUtils.DAY_IN_MILLIS * 30
    private var prefs: SharedPreferences? = null

    var mobilePhonePrefixes: MutableList<String?>
    get()
    {
        val savedTime = getSharedPrefLong(prefs!!, MOBILE_PHONE_PREFIX_DATE)
        val currentTime = System.currentTimeMillis()

        return if(savedTime != 0L && currentTime - savedTime < monthInMills)
        {
            getSharedPrefStringArray(prefs!!, MOBILE_PHONE_PREFIX)
        }
        else //cache not valid anymore or mobilePhonePrefixes empty
        {
            removeArrayList(prefs!!, MOBILE_PHONE_PREFIX)
            mutableListOf()
        }
    }
    set(value)
    {
        setSharedPref(prefs!!, MOBILE_PHONE_PREFIX, value)
        setSharedPref(prefs!!, MOBILE_PHONE_PREFIX_DATE, System.currentTimeMillis())
    }

    companion object {
        @Volatile private var INSTANCE: SharedPrefUtil? = null

        fun getInstance(applicationContext: Context): SharedPrefUtil
        {
            return INSTANCE ?: synchronized(this){
                buildSharedPreferences(applicationContext)
            }
        }

        private fun buildSharedPreferences(applicationContext: Context):SharedPrefUtil{
            INSTANCE = SharedPrefUtil()
            INSTANCE!!.prefs = applicationContext.getSharedPreferences("${applicationContext.packageName}.prefs", Context.MODE_PRIVATE)
            return INSTANCE as SharedPrefUtil
        }
    }


    fun destroyInstance()
    {
        INSTANCE?.prefs = null
        INSTANCE = null

    }
}
