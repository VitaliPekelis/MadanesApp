package com.madanes.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madanes.app.models.VerificationType
import com.madanes.app.models.UserIdentificationInput
import com.madanes.app.utils.AppUtils
import com.madanes.app.views.CallToSupportDialog
import com.madanes.app.views.TextWatcherDelegate
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirstFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

private const val ARG_MOBILE_PHONE_PREFIXES= "arg_mobile_phone_prefixes"

class FirstFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private var mValidationFieldType = VerificationType.PHONE //by default
    private var mFormattedPhoneNumber:String? = null

    private var mMobilePhonePrefixes: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mMobilePhonePrefixes = it.getStringArrayList(ARG_MOBILE_PHONE_PREFIXES)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        setListeners()
    }

    private fun setListeners()
    {
        identification_send_btn.setOnClickListener{
            onSendCodeClick()
        }

        identification_help_btn.setOnClickListener{
            onHelpBtnClick()
        }

        identification_radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.identification_rb_email)
            {
                onEmailRadioBtnClick()

            }
            else if(checkedId == R.id.identification_rb_sms)
            {
                onSmsRadioBtnClick()
            }

            /*set position of cursor*/
            /*val position = identification_mfa_edt.text?.length ?: 0
            identification_mfa_edt.setSelection(position)*/

            identification_mfa_edt.text?.clear()
        }

        identification_user_id_edt.addTextChangedListener(object:TextWatcher by TextWatcherDelegate(){
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                if(s.length == 9)
                {
                    if(isUserIdValid(s.toString()))
                    {
                        identification_userid_valid_iv.visibility = View.VISIBLE
                        identification_user_id_til.error = ""
                        identification_user_id_edt.clearFocus()
                        identification_mfa_edt.requestFocus()
                    }
                    else
                    {
                        identification_user_id_til.error = "User ID Not Valid!!!"
                    }

                }
                else
                {
                    identification_userid_valid_iv.visibility = View.GONE
                }
            }
        })

        identification_mfa_edt.addTextChangedListener(object :TextWatcher by TextWatcherDelegate()
        {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 3 && count > 0)
                {
                    validMobilePhonePrefix(s.toString())
                }
            }
        })

    }

    private fun onHelpBtnClick()
    {
        /*
         *this code need permission
         *
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:036380000"))
        startActivity(intent)

        */

        //No needed permission
        /*val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:036380000"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)*/

        CallToSupportDialog(context!!).showDialog()

    }

    private fun validMobilePhonePrefix(sequence: String) {
        var isValid = false

        mMobilePhonePrefixes?.let {

            for (prefix in it)
            {
                if(sequence == prefix)
                {
                    isValid = true
                    continue
                }

            }
        }

        if(isValid)
        {
            identification_mfa_til.error = ""
        }
        else
        {
            identification_mfa_til.error = "Is not Mobile Phone"
        }
    }

    private fun onSmsRadioBtnClick()
    {
        identification_mfa_til.hint = getString(R.string.hint_sms)
        identification_mfa_edt.inputType = InputType.TYPE_CLASS_PHONE
        mValidationFieldType = VerificationType.PHONE
    }

    private fun onEmailRadioBtnClick()
    {
        identification_mfa_til.hint = getString(R.string.hint_email)
        identification_mfa_edt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        mValidationFieldType = VerificationType.MAIL
    }

    private fun onSendCodeClick()
    {
        val userId = identification_user_id_edt.text.toString()
        val validationValue = identification_mfa_edt.getPhoneNumber()
        val validationType = mValidationFieldType

        if(!isUserIdValid(userId)) {return}

        val isValidationFieldValid:Boolean

        isValidationFieldValid = when(validationType)
        {
            VerificationType.MAIL -> {isEmailValid(validationValue)}
            VerificationType.PHONE -> {validatePhoneNum(validationValue)}
            /*else{true}*/
        }

        if(!isValidationFieldValid) {return}

        mListener?.onSendClick(UserIdentificationInput(userId, validationValue, validationType, mFormattedPhoneNumber))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    private fun isUserIdValid(userId:String) :Boolean
    {
        if(userId.isEmpty())
        {
            identification_user_id_til.error = "Must not be Empty!"
            return false
        }

        if(userId.length != 9)
        {
            identification_user_id_til.error = "Must be 9 Digits!"
            return false
        }

        if(!AppUtils.isIsraeliIdValid(userId))
        {
            identification_user_id_til.error = "UserId Not Valid, Please enter the Right One!"
            return false
        }

        return true
    }
    private fun validatePhoneNum(phoneNumber:String) :Boolean
    {
        if(phoneNumber.isEmpty())
        {
            identification_mfa_til.error = "Must not be Empty!"
            return false
        }

        mFormattedPhoneNumber =
            AppUtils.formatPhoneNumber(phoneNumber) {identification_mfa_til.error = "Phone Number Error"}
                ?: return false


        return true
    }
    private fun isEmailValid(email:String) :Boolean
    {
        if(email.isEmpty())
        {
            identification_mfa_til.error = "Must not be Empty!"
            return false
        }

        if(!AppUtils.isEmailValid(email))
        {
            identification_mfa_til.error = "Email Not Valid, Please enter the Right One!"
            return false
        }

        return true
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onSendClick(userInput: UserIdentificationInput)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment FirstFragment.
         */
        @JvmStatic
        fun newInstance(mobilePhonePrefixes: ArrayList<String?>/*, param2: String*/) =
            FirstFragment() .apply {
               arguments = Bundle().apply {
                    putStringArrayList(ARG_MOBILE_PHONE_PREFIXES, mobilePhonePrefixes)
                    /*putString(ARG_PARAM2, param2)*/
                }
            }
    }
}
