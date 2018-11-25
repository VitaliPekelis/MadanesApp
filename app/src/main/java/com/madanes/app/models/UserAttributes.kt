package com.madanes.app.models

import android.os.Parcel
import android.os.Parcelable


const val USER_PHONE_NUMBER = "phone_number"
const val USER_FIRST_NAME = "given_name"
const val USER_LAST_NAME = "family_name"
const val USER_EMAIL = "email"

data class UserAttributes(
    val userFirstName: String? = null,
    val userLastName: String? = null,
    val userMobilePhoneNumber: String? = null,
    val userEmail: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userFirstName)
        writeString(userLastName)
        writeString(userMobilePhoneNumber)
        writeString(userEmail)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserAttributes> = object : Parcelable.Creator<UserAttributes> {
            override fun createFromParcel(source: Parcel): UserAttributes =
                UserAttributes(source)
            override fun newArray(size: Int): Array<UserAttributes?> = arrayOfNulls(size)
        }
    }
}
