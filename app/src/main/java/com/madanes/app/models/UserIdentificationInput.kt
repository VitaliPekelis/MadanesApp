package com.madanes.app.models


enum class VerificationType(val value: String)
{
    PHONE("sms"),
    MAIL("email")
}

data class  UserIdentificationInput(var id: String,
                                    var verificationValue: String,
                                    var verificationType: VerificationType,
                                    var formattedPhoneNumber:String? = null)
{

    fun getResponseValue(): String
    {
        //"{\"email\":\"pedalka@gmail.com\"}"
        //"{\"sms\":\"+972543222014\"}"
        return when (verificationType)
        {
            VerificationType.MAIL -> "{\"${verificationType.value}\":\"$verificationValue\"}"
            VerificationType.PHONE -> "{\"${verificationType.value}\":\"$formattedPhoneNumber\"}"
        }
    }
}

