package com.cherry.chat.views.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
        setContentView(R.layout.activity_signup)
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        showFirstFragment()
        val firstStageComplete = signUpViewModel.getOtpRequestedLiveData().value ?: false
        if (firstStageComplete) {
            showSecondFragment()
        } else {
            signUpViewModel.getOtpRequestedLiveData().observe({ lifecycle }, { r1 ->
                val result1 = r1 ?: false
                if (result1) {
                    signUpViewModel.getOtpRequestedLiveData().removeObservers({ lifecycle })
                    showSecondFragment()
                    val secondStageComplete = signUpViewModel.getLoginSuccessfulLiveData().value ?: false
                    if (secondStageComplete) {
                        endSignUpFlow()
                    } else {
                        signUpViewModel.getLoginSuccessfulLiveData().observe({ lifecycle }, { r2 ->
                            val result2 = r2 ?: false
                            if (result2) {
                                endSignUpFlow()
                            }

                        })
                    }
                }
            })
        }
    }

    private fun showFirstFragment() {
        val fragment = RequestOTPFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(0, R.anim.slide_out_left, 0, R.anim.slide_out_left)
        transaction.add(R.id.fragment_container, fragment, "page1")
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
        val conversationLaunchIntent = Intent(this, ConversationListActivity::class.java)
        signUpViewModel.getLoginSuccessfulLiveData().removeObservers({ lifecycle })
        startActivity(conversationLaunchIntent)
        finish()
    }
}