package com.cherry.chat.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by girish on 11/19/17.
 */

class SignUpViewModel: ViewModel() {

    companion object {
        const val LOGIN_STATE_STARTING = 0
        const val LOGIN_STATE_OTP_REQUESTED = 2
        const val LOGIN_STATE_VERIFIED = 5
        const val LOGIN_STATE_UNKNOWN = -1

    }

    private var loginStateLiveData: MutableLiveData<Int>? = null

    fun getLoginStateLiveData(): MutableLiveData<Int>
            = loginStateLiveData ?: MutableLiveData<Int>().apply {
        value = LOGIN_STATE_STARTING
    }.also { loginStateLiveData = it }

}