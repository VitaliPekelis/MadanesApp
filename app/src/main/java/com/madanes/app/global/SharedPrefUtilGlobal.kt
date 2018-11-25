package com.madanes.app.global

import android.content.Context
import android.content.SharedPreferences


private const val KEY_DELIMITER = "."

open class SharedPrefUtilGlobal{

    companion object {
        fun getSharedPefString(prefs: SharedPreferences, key: String, dafValue:String? = null): String? {
            return prefs.getString(key, dafValue)
        }

        fun getSharedPefBoolean(prefs: SharedPreferences, key: String, dafValue:Boolean = false): Boolean {
            return prefs.getBoolean(key, dafValue)
        }

        fun getSharedPefFloat(prefs: SharedPreferences, key: String, defValue:Float = 0.0f): Float {
            return prefs.getFloat(key, defValue)
        }

        fun getSharedPrefLong(prefs: SharedPreferences, key: String, defValue:Long = 0L):Long {
            return prefs.getLong(key, defValue)
        }

        fun getSharedPefInt(prefs: SharedPreferences, key: String, defValue:Int = 0): Int {
            return prefs.getInt(key, defValue)
        }

        fun getSharedPefStringSet(prefs: SharedPreferences, key: String, defValue:Set<String>? = null): Set<String>? {
            return prefs.getStringSet(key, defValue)
        }

        fun getSharedPrefStringArray(prefs: SharedPreferences, key: String): MutableList<String?> {
            val result:MutableList<String?> = mutableListOf()

            var index = 0
            var nextKey = getArrayKey(prefs, key, index)

            while (prefs.contains(nextKey)) {
                result.add(prefs.getString(nextKey, null))
                index++
                nextKey = getArrayKey(prefs, key, index)
            }

            return result
        }

        fun setSharedPref(prefs: SharedPreferences, key: String, value: MutableList<String?>) {
            val editor = prefs.edit()

            removeArrayList(prefs, key)

            value.let{
                for (i in 0 until it.size) {
                    editor.putString(getArrayKey(prefs, key, i), it[i])
                }
                editor.apply()
            }
        }

        fun setSharedPref(prefs: SharedPreferences, key: String, value: Long) {
            val editor = prefs.edit()
            editor.putLong(key, value).apply()
        }
        fun setSharedPref(prefs: SharedPreferences, key: String, value: String) {
            val editor = prefs.edit()
            editor.putString(key, value).apply()
        }
        fun setSharedPref(prefs: SharedPreferences, key: String, value: Boolean) {
            val editor = prefs.edit()
            editor.putBoolean(key, value).apply()
        }
        fun setSharedPref(prefs: SharedPreferences, key: String, value: Float) {
            val editor = prefs.edit()
            editor.putFloat(key, value).apply()
        }
        fun setSharedPref(prefs: SharedPreferences,context: Context, key: String, value: Int) {
            val editor = prefs.edit()
            editor.putInt(key, value).apply()
        }
        fun setSharedPref(prefs: SharedPreferences, key: String, value: Set<String>) {
            val editor = prefs.edit()
            editor.putStringSet(key, value).apply()
        }
        private fun getArrayKey(prefs: SharedPreferences, key: String, index: Int): String {
            return "$key$KEY_DELIMITER$index"
        }
        fun removeArrayList(prefs: SharedPreferences, key: String) {
            val editor = prefs.edit()
            var nextIndex = 0
            var keyToRemove = getArrayKey(prefs, key, nextIndex)
            while (prefs.contains(keyToRemove)) {
                editor.remove(keyToRemove)
                nextIndex++
                keyToRemove = getArrayKey(prefs, key, nextIndex)
            }

            editor.apply()
        }
    }
}
