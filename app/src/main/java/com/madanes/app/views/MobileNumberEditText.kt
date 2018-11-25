package com.madanes.app.views

import android.content.Context
import android.text.Selection
import android.text.TextWatcher
import android.util.AttributeSet

class MobileNumberEditText: CleanableTextInputEditText
{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)


    private var currentString:String = ""
    private var phoneNumber:String = ""

    init {

        addTextChangedListener(object:TextWatcher by TextWatcherDelegate()
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                if(s.toString() != currentString)
                {
                    this@MobileNumberEditText.removeTextChangedListener(this)

                    phoneNumber = s.toString().replace("-", "")

                    if (start == 2 && count > 0)
                    {
                        val formattedBuilder  = StringBuilder(phoneNumber)
                        formattedBuilder.append("-")
                        val newValue = formattedBuilder.toString()
                        this@MobileNumberEditText.setText(newValue)
                        currentString = newValue
                        Selection.setSelection(this@MobileNumberEditText.text, newValue.length)
                    }


                    this@MobileNumberEditText.addTextChangedListener(this)
                }
            }
        })
    }


    fun getPhoneNumber(): String {
        return phoneNumber
    }
}