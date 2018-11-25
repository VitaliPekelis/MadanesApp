package com.madanes.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.madanes.app.utils.Logger


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : Activity() {

    private lateinit var mHandler:Handler

    private val runnable = Runnable {
        val intent = Intent(this@SplashActivity, IdentityActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(savedInstanceState == null)
        {
            mHandler = Handler(Looper.getMainLooper())
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?)
    {
        super.onPostCreate(savedInstanceState)
        Logger.logDebug()
    }


    override fun onResume() {
        setFullScreen()
        super.onResume()
    }

    override fun onStart() {
        super.onStart()

        mHandler.postDelayed(runnable, 3000)

    }

    override fun onPause() {
        mHandler.removeCallbacks(runnable)
        super.onPause()
    }

    private fun setFullScreen() {
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = uiOptions
    }
}
