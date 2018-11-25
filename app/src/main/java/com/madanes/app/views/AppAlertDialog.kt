package com.madanes.app.views

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

import com.madanes.app.R


open class AppAlertDialog(val context: Context)
{
    private var mDialog: Dialog
    val contentLayout: FrameLayout

    init {
        val customLayout = LayoutInflater.from(context).inflate(R.layout.base_dialog_layout, null, false)
        contentLayout = customLayout.findViewById(R.id.content_container)
        mDialog = Dialog(context, R.style.App_Dialog)
        mDialog.apply {
            setContentView(customLayout)
        }

        val close = customLayout.findViewById(R.id.dialog_iv_close) as ImageView
        close.setOnClickListener {
            mDialog.dismiss()
        }
    }

    fun showDialog() {
        mDialog.show()
    }

    fun dismissDilaog() {
        mDialog.dismiss()
    }

    fun setContentView(layoutResId: Int):AppAlertDialog {

            val view = LayoutInflater.from(context).inflate(layoutResId, contentLayout, true)
            setContentView(view)

        return this
    }

    fun setContentView(v: View) {
        if (contentLayout.childCount == 0)
        // only one content
        {
            contentLayout.addView(v)
        }
    }
}