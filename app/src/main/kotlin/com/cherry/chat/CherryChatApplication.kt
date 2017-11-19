package com.cherry.chat

import android.app.Application
import com.cherry.core.Cherry

/**
 * Created by girish on 11/19/17.
 */

class CherryChatApplication: Application() {

    companion object {
        const val PARTNER_ID = ""
    }

    override fun onCreate() {
        super.onCreate()
        Cherry.init(this, PARTNER_ID)
    }

}