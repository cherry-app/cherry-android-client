package com.cherry.chat.views.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
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
        btnFinish.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        Cherry.Session.verifyOtp(otp, { result, exception ->
            if (result) {
                val signUpViewModel = ViewModelProviders.of(activity).get(SignUpViewModel::class.java)
                signUpViewModel.getLoginStateLiveData().value = SignUpViewModel.LOGIN_STATE_VERIFIED
            } else {
                val errorMessage = if (exception == null) {
                    "Incorrect OTP entered. Please check the OTP and try again."
                } else {
                    "There was some problem while verifying the OTP. Please check your internet connection."
                }
                val fragmentView = view
                if (fragmentView != null) {
                    Snackbar.make(fragmentView, errorMessage, Snackbar.LENGTH_LONG).show()
                }
            }
            progressBar.visibility = View.INVISIBLE
            btnFinish.visibility = View.VISIBLE
        })
    }

}