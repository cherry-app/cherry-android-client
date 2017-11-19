package com.cherry.chat.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by girish on 11/19/17.
 */

class SignUpViewModel: ViewModel() {

    private var otpRequestedLiveData: MutableLiveData<Boolean>? = null
    private var loginSuccessful: MutableLiveData<Boolean>? = null

    fun getOtpRequestedLiveData(): MutableLiveData<Boolean> = otpRequestedLiveData ?: MutableLiveData<Boolean>().also { otpRequestedLiveData = it }

    fun getLoginSuccessfulLiveData(): MutableLiveData<Boolean> = loginSuccessful ?: MutableLiveData<Boolean>().also { loginSuccessful = it }


}