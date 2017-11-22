package com.cherry.chat.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.cherry.core.Cherry
import com.cherry.core.models.ConversationWithParticipant

/**
 * Created by girish on 11/20/17.
 */

class ConversationViewModel: ViewModel() {

    fun getConversationLiveData(context: Context): LiveData<List<ConversationWithParticipant>> =
            Cherry.Messaging.getConversationLiveData(context)
}