package com.cherry.chat

import android.app.Application
import com.cherry.core.Cherry

/**
 * Created by girish on 11/19/17.
 */

class CherryChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Cherry.init(this, BuildConfig.CHERRY_CHAT_PARTNER_ID)
    }

}