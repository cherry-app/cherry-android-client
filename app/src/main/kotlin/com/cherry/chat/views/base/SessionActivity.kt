package com.cherry.chat.views.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.cherry.chat.managers.SharedPreferenceManager
import com.cherry.chat.views.activities.SignUpActivity
import com.cherry.core.Cherry
import com.cherry.core.interfaces.SessionClosed

/**
 * Created by kavins on 11/28/17.
 */

open class SessionActivity: AppCompatActivity(), SessionClosed{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Cherry.Session.registerSessionClosedListener(this)
    }

    override fun onStop() {
        super.onStop()
        Cherry.Session.unRegisterSessionClosedListener()
    }

    override fun onSessionClosed() {
        logoutAndLaunchSignUp(false)
    }

    fun logoutAndLaunchSignUp(showToast: Boolean) {
        Cherry.Session.uid = null
        Cherry.Session.sessionToken = null
        Cherry.Session.loginToken = null

        SharedPreferenceManager.setPhoneNumber(this, "")
        SharedPreferenceManager.setUserName(this, "")

        if (showToast)
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()

        val launchIntent = Intent(this, SignUpActivity::class.java)
        startActivity(launchIntent)

        finishAffinity()
    }
}