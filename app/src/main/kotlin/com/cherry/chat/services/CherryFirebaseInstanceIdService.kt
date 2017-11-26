package com.cherry.chat.services

import com.cherry.core.Cherry
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by girish on 11/26/17.
 */

class CherryFirebaseInstanceIdService: FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val firebaseToken = FirebaseInstanceId.getInstance().token ?: return
        Cherry.Session.updateFirebaseToken(firebaseToken, { success ->
            // TODO If not successful, need to handle retry logic

        })
    }
}