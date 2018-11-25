package com.madanes.app

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.madanes.app.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_insurances.*

class InsurancesActivity : AppCompatActivity() ,InsurancesFragment.OnFragmentInteractionListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurances)


        if(savedInstanceState == null)
        {
            InsurancesFragment.newInstance().also {
                replaceFragment(insurances_fragment_container.id, it)
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // InsurancesFragment.OnFragmentInteractionListener - implementation
    //------------------------------------------------------------------------------------------------------------------
    override fun onFragmentInteraction(uri: Uri)
    {

    }
}
