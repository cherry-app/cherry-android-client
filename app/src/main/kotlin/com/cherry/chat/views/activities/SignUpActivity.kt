package com.cherry.chat.views.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cherry.chat.R
import com.cherry.chat.viewmodels.SignUpViewModel
import com.cherry.chat.views.fragments.RequestOTPFragment
import com.cherry.chat.views.fragments.VerifyOTPFragment

/**
 * Created by girish on 11/19/17.
 */

class SignUpActivity: AppCompatActivity() {

    lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_signup)
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        val currentState = signUpViewModel.getLoginStateLiveData().value ?: SignUpViewModel.LOGIN_STATE_UNKNOWN
        handleState(currentState)
        observe()
    }

    private fun observe() {
        signUpViewModel.getLoginStateLiveData().observeForever { newState ->
            val state = newState ?: SignUpViewModel.LOGIN_STATE_UNKNOWN
            handleState(state)
        }
    }

    private fun handleState(state: Int) {
        when(state) {
            SignUpViewModel.LOGIN_STATE_STARTING -> showFirstFragment()
            SignUpViewModel.LOGIN_STATE_OTP_REQUESTED -> showSecondFragment()
            SignUpViewModel.LOGIN_STATE_VERIFIED -> endSignUpFlow()
            else -> reset()
        }
    }

    private fun reset() {
        signUpViewModel.getLoginStateLiveData().value = SignUpViewModel.LOGIN_STATE_STARTING
    }

    private fun showFirstFragment() {
        val fragment = RequestOTPFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(0, R.anim.slide_out_left, 0, R.anim.slide_out_left)
        transaction.replace(R.id.fragment_container, fragment, "page1")
        transaction.commit()
    }

    private fun showSecondFragment() {
        val fragment = VerifyOTPFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.replace(R.id.fragment_container, fragment, "page2")
        transaction.commit()
    }

    private fun endSignUpFlow() {
        signUpViewModel.getLoginStateLiveData().removeObservers({ lifecycle })
        val conversationLaunchIntent = Intent(this, ConversationListActivity::class.java)
        startActivity(conversationLaunchIntent)
        finish()
    }
}