package com.cherry.chat.services

import com.cherry.core.Cherry
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by girish on 11/26/17.
 */

class CherryFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        message ?: return
        val data = message.data
        Cherry.Messaging.processData(this, data)
    }

}