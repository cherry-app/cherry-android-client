package com.cherry.chat.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cherry.core.Cherry

/**
 * Created by girish on 11/19/17.
 */

/**
 * This activity is intended only for deciding which activity should be launched subsequently
 * This activity has no UI and only acts as a trampoline
 * **/
class LaunchActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Cherry.Session.isLoggedIn) {
            val launchIntent = Intent(this, ConversationListActivity::class.java)
            startActivity(launchIntent)
        } else {
            val launchIntent = Intent(this, SignUpActivity::class.java)
            startActivity(launchIntent)
        }
        finish()
    }

}