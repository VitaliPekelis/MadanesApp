package com.madanes.app.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.madanes.app.R
import kotlinx.android.synthetic.main.verification_code_layout.view.*


class VerificationCodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0): LinearLayout(context, attrs, defStyle, defStyleRes)
{
    private var mOnKeyListener: View.OnKeyListener?
    private var mOnTouchListener:View.OnTouchListener?
    private var code: CharArray = CharArray(4)

    var viewListener: IOnViewInteraction? = null


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.verification_code_layout, this, true)

        orientation = HORIZONTAL
        clipToPadding = false

        mOnKeyListener = OnKeyListener { v, keyCode, event ->
            if (event?.action != KeyEvent.ACTION_DOWN)
                return@OnKeyListener true

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                when (v?.id) {

                    R.id.number_1_edt -> {
                        code[0] = Character.MIN_VALUE
                    }
                    R.id.number_2_edt -> {
                        code[1] = Character.MIN_VALUE
                        number_2_edt.clearFocus()
                        number_1_edt.requestFocus()
                    }
                    R.id.number_3_edt -> {
                        code[2] = Character.MIN_VALUE
                        number_3_edt.clearFocus()
                        number_2_edt.requestFocus()
                    }
                    R.id.number_4_edt -> {
                        code[3] = Character.MIN_VALUE
                        number_4_edt.clearFocus()
                        number_3_edt.requestFocus()
                    }
                    else -> {
                        //default value
                    }
                }

                v.setBackgroundResource(R.drawable.bg_edt_code_normal)
            }

            false
        }

        mOnTouchListener = OnTouchListener{v, event ->

            if(event.action == MotionEvent.ACTION_DOWN)
            {
                return@OnTouchListener false
            }

            return@OnTouchListener true
        }
    }

    fun getCode():String = String(code)
    fun setCode(number1:Char, number2:Char, number3:Char, number4:Char)
    {
        var isDigit = true

        code.run{
            if (number1.isDigit())
            {
                this[0] = number1
                number_1_edt.setText(this, 0,1)
            }
            else{
                isDigit = false
            }
            if (number2.isDigit())
            {
                this[1] = number2
                number_2_edt.setText(this, 1,1)
            }
            else{
                isDigit = false
            }
            if (number3.isDigit())
            {
                this[2] = number3
                number_3_edt.setText(this, 2,1)
            }
            else{
                isDigit = false
            }
            if (number4.isDigit())
            {
                this[3] = number4
                number_4_edt.setText(this, 3,1)
            }
            else{
                isDigit = false
            }
        }

        if(isDigit)
        {
            viewListener?.onInputCodeFinished(String(code))
        }

    }

    private fun setListeners() {

        number_1_edt.addTextChangedListener(object:TextWatcher by TextWatcherDelegate(){
            override fun afterTextChanged(s: Editable)
            {
                if (s.length == 1)
                {
                    code[0] = s[0]
                    number_1_edt.clearFocus()
                    number_2_edt.requestFocus()
                    number_1_edt.setBackgroundResource(R.drawable.bg_edt_code_not_empty)
                }
            }
        })

        number_1_edt.setOnKeyListener(mOnKeyListener)

        number_2_edt.addTextChangedListener(object:TextWatcher by TextWatcherDelegate(){
            override fun afterTextChanged(s: Editable)
            {
                if (s.length == 1)
                {
                    code[1] = s[0]
                    number_2_edt.clearFocus()
                    number_3_edt.requestFocus()
                    number_2_edt.setBackgroundResource(R.drawable.bg_edt_code_not_empty)
                }
            }
        })

        number_2_edt.setOnKeyListener(mOnKeyListener)
        number_2_edt.setOnTouchListener(mOnTouchListener)

        number_3_edt.addTextChangedListener(object:TextWatcher by TextWatcherDelegate(){
            override fun afterTextChanged(s: Editable)
            {
                if (s.length == 1)
                {
                    code[2] = s[0]
                    number_3_edt.clearFocus()
                    number_4_edt.requestFocus()
                    number_3_edt.setBackgroundResource(R.drawable.bg_edt_code_not_empty)
                }
            }
        })

        number_3_edt.setOnKeyListener (mOnKeyListener)
        number_3_edt.setOnTouchListener(mOnTouchListener)

        number_4_edt.addTextChangedListener(object:TextWatcher by TextWatcherDelegate(){
            override fun afterTextChanged(s: Editable)
            {
                if (s.length == 1)
                {
                    code[3] = s[0]
                    viewListener?.onInputCodeFinished(String(code))
                    number_4_edt.setBackgroundResource(R.drawable.bg_edt_code_not_empty)
                }
            }
        })

        number_4_edt.setOnKeyListener (mOnKeyListener)
        number_4_edt.setOnTouchListener(mOnTouchListener)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setListeners()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mOnKeyListener = null
        mOnTouchListener = null
        viewListener = null
    }



    interface IOnViewInteraction
    {
        fun onInputCodeFinished(code: String)
    }
}