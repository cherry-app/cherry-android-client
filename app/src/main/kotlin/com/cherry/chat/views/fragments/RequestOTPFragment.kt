package com.cherry.chat.views.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cherry.chat.R
import com.cherry.chat.viewmodels.SignUpViewModel
import com.cherry.core.Cherry
import kotlinx.android.synthetic.main.signup_part_1.*

/**
 * Created by girish on 11/19/17.
 */

class RequestOTPFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater ?: return null
        return inflater.inflate(R.layout.signup_part_1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSignUp.setOnClickListener {
            requestOtp()
        }
    }

    private fun requestOtp() {
        val phoneNumber = etPhone.text.toString()
        val name = etName.text.toString().trim()
        if (phoneNumber.length != 10 || !TextUtils.isDigitsOnly(phoneNumber)) {
            return
        }
        if (name.length < 2) {
            return
        }
        Cherry.Session.requestOtp(phoneNumber, name, { result ->
            Log.d("Cherry", "OTP callback")
            if (result) {
                Log.d("Cherry", "OTP success")
                val signUpViewModel = ViewModelProviders.of(activity).get(SignUpViewModel::class.java)
                signUpViewModel.getOtpRequestedLiveData().value = true
            } else {
                Log.d("Cherry", "OTP fail")
            }
        })
    }
}