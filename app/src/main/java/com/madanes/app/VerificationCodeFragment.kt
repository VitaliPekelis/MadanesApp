package com.madanes.app

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Interpolator
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.madanes.app.models.UserAttributes
import com.madanes.app.utils.hideKeyboard
import com.madanes.app.views.VerificationCodeView
import kotlinx.android.synthetic.main.fragment_verification_code.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [VerificationCodeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [VerificationCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */


private const val ARG_USER_ATTRIBUTES = "arg_user_attributes"

class VerificationCodeFragment : Fragment() {

    private var mUserAttributes: UserAttributes? = null
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mUserAttributes = it.getParcelable(ARG_USER_ATTRIBUTES)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        /*verification_code_send_btn.setOnClickListener{
            onSendCodeClick()
        }*/

        verification_greeting_tv.setText(String.format(getString(R.string.greeting, mUserAttributes?.userFirstName)))
        verification_type_tv.setText(mUserAttributes?.userMobilePhoneNumber ?: mUserAttributes?.userEmail)


        verification_code_view.viewListener = object : VerificationCodeView.IOnViewInteraction {
            override fun onInputCodeFinished(code: String)
            {
                Toast.makeText(context, "Code is: $code",Toast.LENGTH_SHORT).show()
                hideKeyboard()
                mListener?.onSendVerifyCodeClick(code)
            }
        }

        verification_send_code_again_tv.setOnClickListener{
            /*it.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.move
                ))*/
            val bannerView = banner

            // to animate left to right
            val lftToRgt = ObjectAnimator.ofFloat(
                bannerView, "translationX", 0f, bannerView.width.toFloat()).apply {
                duration = 500
                interpolator = FastOutSlowInInterpolator()
                startDelay = 1500L
            }

            // to animate right to left
            val rgtToLft = ObjectAnimator.ofFloat(
                bannerView,"translationX", bannerView.width.toFloat(), 0f).apply {
                duration = 500
                interpolator = FastOutSlowInInterpolator()
            }

            val animatorSet = AnimatorSet()

            animatorSet.play(rgtToLft).before(lftToRgt) // manage sequence
            animatorSet.start()
        }

        //verification_code_view.setCode('1','2', '3', '4')
    }

    /*private fun onSendCodeClick()
    {
        mListener?.onSendVerifyCodeClick(verification_code_edt.text.toString())
    }*/

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onSendVerifyCodeClick(code:String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment VerificationCodeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userAttributes: UserAttributes) =
            VerificationCodeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER_ATTRIBUTES, userAttributes)
                    /*putString(ARG_PARAM2, param2)*/
                }
            }
    }
}
