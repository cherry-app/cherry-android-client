package com.cherry.chat.views.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cherry.chat.R
import com.cherry.chat.managers.SharedPreferenceManager
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

        loadDefaults()

        btnSignUp.setOnClickListener {
            requestOtp()
        }
        tvHaveOTP.setOnClickListener {
            alreadyHaveOtp()
        }
    }

    private fun loadDefaults() {
        val phoneNumber = SharedPreferenceManager.getPhoneNumber(context)
        val userName = SharedPreferenceManager.getUserName(context)

        if(phoneNumber != null) {
            etPhone.setText(phoneNumber)
        }
        if(userName!= null) {
            etName.setText(userName)
        }
    }

    private fun alreadyHaveOtp() {
        val signUpViewModel = ViewModelProviders.of(activity).get(SignUpViewModel::class.java)
        signUpViewModel.getLoginStateLiveData().value = SignUpViewModel.LOGIN_STATE_OTP_REQUESTED
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
        btnSignUp.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        val phoneNumberWithCode = "+91" + phoneNumber
        Cherry.Session.requestOtp(phoneNumberWithCode, name, { result, exception ->
            if (result) {
                SharedPreferenceManager.setPhoneNumber(context, phoneNumberWithCode)
                SharedPreferenceManager.setUserName(context, name)

                val signUpViewModel = ViewModelProviders.of(activity).get(SignUpViewModel::class.java)
                signUpViewModel.getLoginStateLiveData().value = SignUpViewModel.LOGIN_STATE_OTP_REQUESTED
            } else {
                val errorMessage = if (exception == null) {
                    "OTP could not be sent. Please check if the number is valid."
                } else {
                    "There was some problem while sending the OTP. Please check your internet connection."
                }
                val fragmentView = view
                if (fragmentView != null) {
                    Snackbar.make(fragmentView, errorMessage, Snackbar.LENGTH_LONG).show()
                }
            }
            progressBar.visibility = View.INVISIBLE
            btnSignUp.visibility = View.VISIBLE
        })
    }
}