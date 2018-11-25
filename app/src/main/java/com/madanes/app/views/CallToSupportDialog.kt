package com.madanes.app.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import com.madanes.app.R
import com.madanes.app.R.id.cancel_action
import com.madanes.app.R.id.view
import kotlinx.android.synthetic.main.call_to_support_layout.view.*

class CallToSupportDialog(context: Context) : AppAlertDialog(context)
{
    init {
        setContentView(R.layout.call_to_support_layout)
        contentLayout.call_to_support_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:036380000"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            dismissDilaog()
        }
    }
}