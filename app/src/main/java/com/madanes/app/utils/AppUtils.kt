package com.madanes.app.utils

import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.regex.Pattern
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.google.i18n.phonenumbers.Phonenumber
import com.madanes.app.BuildConfig


object AppUtils {

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    fun isEmailValid(email: String): Boolean
    {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    fun isIsraeliIdValid(value: String): Boolean
    {
        var multiplier = 1
        var sum = 0

        if (value.isEmpty()) return false

        var id: Int = value.toIntOrNull() ?: return false
        if (id == 0) return false

        while (id > 0)
        {
            val num = (id % 10 * multiplier).toLong()
            sum += (if (num >= 10) num % 10 + num / 10 else num).toInt()
            multiplier = 2 / multiplier
            id /= 10
        }

        return sum % 10 == 0
    }


    fun formatPhoneNumber(phoneNumStr:String, error: () -> Unit):String?
    {
        val phoneUtil = PhoneNumberUtil.getInstance()

        val phoneNumber: Phonenumber.PhoneNumber
        try
        {
            phoneNumber = phoneUtil.parse(phoneNumStr, "IL")
        }
        catch (e: Exception)
        {
            if(BuildConfig.DEBUG) e.printStackTrace()
            error()
            return null
        }

        return if (phoneUtil.isValidNumber(phoneNumber))
        {
            phoneUtil.format(
                phoneNumber,
                PhoneNumberFormat.E164)
        }
        else
        {
            error()
            null
        }
    }
}