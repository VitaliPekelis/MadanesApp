package com.madanes.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.madanes.app.aws.*
import com.madanes.app.global.App
import com.madanes.app.models.UserAttributes
import com.madanes.app.models.UserIdentificationInput
import com.madanes.app.network.NetworkHandler
import com.madanes.app.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class IdentityActivity : AppCompatActivity(),
                                    FirstFragment.OnFragmentInteractionListener,
                                    VerificationCodeFragment.OnFragmentInteractionListener
{
    private val mIdentityHelper: CognitoIdentityHelper by lazy {
        CognitoIdentityHelper.getInstance(this.applicationContext)
    }

    private val mAuthenticationHandler = object : AuthenticationHandler
    {
        override fun onSuccess(userSession: CognitoUserSession,
                               newDevice: CognitoDevice?)
        {
            AlertDialog.Builder(this@IdentityActivity)
                .setMessage(" userName: ${userSession.username} Success!!")
                .setTitle("Authentication")
                .setNegativeButton("Ok")
                { dialog, _ ->               //on Ok Click
                    popFragmentBackStack()
                    dialog.dismiss()
                }
                .create().show()


            userSession.username

            /*if (BuildConfig.DEBUG) Logger.logDebug("Authentication", """Success newDevice: ${newDevice.toString()}
                                        accessToken.expiration = ${userSession?.accessToken?.expiration}
                                        accessToken.jwtToken = ${userSession?.accessToken?.jwtToken}
                                        accessToken.username = ${userSession?.accessToken?.username}
                                        refreshToken.token = ${userSession?.refreshToken?.token}
                                        isValidForThreshold = ${userSession?.isValidForThreshold}
                                        isValid = ${userSession?.isValid}
                                        username = ${userSession?.username}
                                        """)*/


            mIdentityHelper.userPool.getUser(userSession.username).getDetailsInBackground(
                object: GetDetailsHandler{
                    override fun onSuccess(cognitoUserDetails: CognitoUserDetails?)
                    {
                        //Todo implement get user details
                        val attributesMap = cognitoUserDetails?.attributes?.attributes!!
                        Logger.logInfo(logText = attributesMap.toString())
                    }

                    override fun onFailure(exception: Exception?)
                    {
                        exception?.printStackTrace()
                    }

                }
            )
        }

        override fun onFailure(exception: Exception?)
        {
            exception?.printStackTrace()

            if(BuildConfig.DEBUG)
            {
                AlertDialog.Builder(this@IdentityActivity)
                    .setMessage(exception?.message)
                    .setTitle("Authentication Exception")
                    .setNegativeButton("Ok")
                    { dialog, _ ->               //on Ok Click
                        dialog.dismiss()
                    }
                    .create().show()
            }

        }

        override fun getAuthenticationDetails(
            authenticationContinuation: AuthenticationContinuation,
            userId: String)
        {
            //if (BuildConfig.DEBUG) Logger.logDebug()
            mIdentityHelper.onGetAuthenticationDetailsArrived(authenticationContinuation, userId)
        }


        override fun authenticationChallenge(continuation: ChallengeContinuation)
        {
            if (CUSTOM_CHALLENGE == continuation.challengeName)
            {
                mIdentityHelper.continuation = continuation

                when(continuation.parameters[NEXT])
                {
                    SET_TRANSPORT -> {mIdentityHelper.setTypeVerification()}
                    VERIFY_CODE -> {openVerificationCodeScreen()}
                    VERIFY_CODE_AGAIN -> {showErrorCode()}
                    else ->
                    {
                        if(BuildConfig.DEBUG) Logger.logError("CUSTOM_CHALLENGE NOT_HANDLED", continuation.toString())
                    }
                }
            }
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?)
        {
            if (BuildConfig.DEBUG) Logger.logDebug()
        }
    }


    private lateinit var mMobilePhonePrefixes:MutableList<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMobilePhonePrefixes = getMobilePhonePrefixes()

        if(savedInstanceState == null)
        {

            /*FirstFragment.newInstance(ArrayList(mMobilePhonePrefixes)).also {
                replaceFragment(fragment_container.id, it)
            }*/

            openVerificationCodeScreen()

            mIdentityHelper.findCurrentUser(mAuthenticationHandler)
        }
    }

    private fun getMobilePhonePrefixes(): MutableList<String?>
    {
        val app = application as App
        var result = app.sharedPrefs.mobilePhonePrefixes
        if(result.isEmpty())
        {
            NetworkHandler.getMobilePhonePrefix().enqueue(object : Callback<MutableList<String?>?>
            {
                override fun onFailure(call: Call<MutableList<String?>?>, t: Throwable)
                {
                    //todo handle implement error

                    t.printStackTrace()

                    AlertDialog.Builder(this@IdentityActivity)
                        .setMessage(t.message)
                        .setTitle("Error!!")
                        .setNegativeButton("Ok")
                        { dialog, _ ->               //on Ok Click
                            dialog.dismiss()
                        }
                        .create().show()

                    result = mutableListOf()
                }

                override fun onResponse(call: Call<MutableList<String?>?>, response: Response<MutableList<String?>?>)
                {
                    val res = response.body()
                    res?.let{
                        app.sharedPrefs.mobilePhonePrefixes = it
                        result = it
                    }
                }

            })
        }

        return result
    }


    fun openVerificationCodeScreen() {

        //TODO change to real data
        val userAttributes = UserAttributes(
            userFirstName = "דני",
            userLastName = "גוסיס",
            userEmail = "d*****@g***.com",
            userMobilePhoneNumber = "054-***-**12"
        )

        VerificationCodeFragment.newInstance(userAttributes).also {
            addFragment(fragment_container.id, it)

        }
    }

    fun showErrorCode() {
        AlertDialog.Builder(this@IdentityActivity)
            .setMessage("Try Again Please!")
            .setTitle("Wrong Code")
            .setNegativeButton("Ok")
            { dialog, _ ->               //on Ok Click
                dialog.dismiss()
            }
            .create().show()
    }


    //------------------------------------------------------------------------------------------------------------------
    // FirstFragment.OnFragmentInteractionListener - implementation
    //------------------------------------------------------------------------------------------------------------------
    override fun onSendClick(userInput: UserIdentificationInput)
    {
        mIdentityHelper.userInput = userInput
        mIdentityHelper.userPool.getUser(userInput.id)?.getSessionInBackground(mAuthenticationHandler)
    }

    //------------------------------------------------------------------------------------------------------------------
    // VerificationCodeFragment.OnFragmentInteractionListener - implementation
    //------------------------------------------------------------------------------------------------------------------
    override fun onSendVerifyCodeClick(code: String)
    {
        mIdentityHelper.sendVerificationCode(code)
    }
}
