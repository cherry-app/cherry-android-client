package com.cherry.chat.views.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cherry.chat.R
import com.cherry.chat.viewmodels.SignUpViewModel
import com.cherry.core.Cherry
import kotlinx.android.synthetic.main.signup_part_2.*


/**
 * Created by girish on 11/19/17.
 */

class VerifyOTPFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater ?: return null
        return inflater.inflate(R.layout.signup_part_2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFinish.setOnClickListener {
            verifyOtp()
        }
    }

    private fun verifyOtp() {
        val otp = etOtp.text.toString()
        if (otp.length < 6) {
            return
        }
        Cherry.Session.verifyOtp(otp, { result ->
            if (result) {
                val signUpViewModel = ViewModelProviders.of(activity).get(SignUpViewModel::class.java)
                signUpViewModel.getLoginSuccessfulLiveData().value = true
            }
        })
    }

}