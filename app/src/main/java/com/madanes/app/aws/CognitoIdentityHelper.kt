package com.madanes.app.aws

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoServiceConstants
import com.amazonaws.regions.Regions
import com.madanes.app.R
import com.madanes.app.models.UserIdentificationInput
import com.madanes.app.utils.Logger

const val ANSWER = "ANSWER"
const val USERNAME = "USERNAME"
const val CUSTOM_CHALLENGE = "CUSTOM_CHALLENGE"
const val NEXT = "NEXT"
const val SET_TRANSPORT = "SET_TRANSPORT"
const val VERIFY_CODE = "VERIFY_CODE"
const val VERIFY_CODE_AGAIN = "VERIFY_CODE_AGAIN"

class CognitoIdentityHelper private constructor(context: Context)
{
    private val userPoolId:String
    private val clientId:String
    /**
     * App secret associated with your app id - if the App id does not have an associated App secret,
     * set the App secret to null.
     * [mClientSecret] = null;
     */
    private val mClientSecret:String?

    /**
     * User Pools region.
     */
    private val cognitoRegion = Regions.EU_WEST_1

    val userPool: CognitoUserPool

    /*Challenge*/
    var continuation: ChallengeContinuation? = null

    /*User*/
    var userInput: UserIdentificationInput? = null

    init
    {
        val resources = context.resources
        userPoolId = resources.getString(R.string.userPoolId)
        clientId = resources.getString(R.string.clientId)
        mClientSecret  = null
        userPool = CognitoUserPool(context, userPoolId, clientId, mClientSecret, cognitoRegion)
    }


    fun findCurrentUser(authenticationHandler: AuthenticationHandler)
    {
        val user = userPool.currentUser
        userInput?.id = user.userId


        //TODO Vitali remove this code later
        /*userId?.let {
            user.getSessionInBackground(authenticationHandler)
        }*/

        user.signOut()
    }

    fun onGetAuthenticationDetailsArrived(authenticationContinuation: AuthenticationContinuation, userId: String)
    {
        userInput?.id = userId
        /*val password = userId*/

        val authParams = mutableMapOf(USERNAME to userId)

        val validationData :MutableMap <String, String> = mutableMapOf()

        /*val authenticationDetails = AuthenticationDetails(userId, password, null)*/

        val authenticationDetails = AuthenticationDetails(userId, authParams, validationData)
        authenticationDetails.setCustomChallenge(CognitoServiceConstants.CHLG_TYPE_CUSTOM_CHALLENGE)

        authenticationContinuation.setAuthenticationDetails(authenticationDetails)
        authenticationContinuation.continueTask()
    }

    fun setTypeVerification()
    {
        continuation?.setChallengeResponse(ANSWER, userInput?.getResponseValue())
        continuation?.continueTask()
    }

    fun sendVerificationCode(code:String)
    {
        continuation?.setChallengeResponse(ANSWER, code)
        continuation?.continueTask()
    }

    companion object
    {
        private var instance : CognitoIdentityHelper? = null

        @JvmStatic
        @Synchronized fun getInstance(context: Context) : CognitoIdentityHelper
        {
            return instance ?: CognitoIdentityHelper(context).also { instance = it }
        }
    }

}