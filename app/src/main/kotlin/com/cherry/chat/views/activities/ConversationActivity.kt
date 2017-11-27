package com.cherry.chat.views.activities

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import com.cherry.chat.CherryChatApplication
import com.cherry.chat.R
import com.cherry.chat.views.adapters.PagedMessageListAdapter
import com.cherry.chat.views.base.SessionActivity
import com.cherry.core.Cherry
import com.cherry.core.models.Message
import com.cherry.core.models.Participant
import kotlinx.android.synthetic.main.activity_conversation.*

/**
 * Created by girish on 11/21/17.
 */

class ConversationActivity : SessionActivity() {

    companion object {
        const val KEY_PARTICIPANT_ID = "participantId"
        const val KEY_PARTICIPANT = "participant"
    }

    private var mParticipant: Participant? = null
    private var mParticipantId: String? = null

    private val mBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return
            intent ?: return
            if (intent.action == Cherry.ACTION_NEW_INCOMING_MESSAGE) {
                val message = intent.getSerializableExtra(Cherry.KEY_MESSAGE) as? Message
                if (message != null) {
                Cherry.Messaging.markAsRead(context, message.senderId)
                }
                abortBroadcast()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        mParticipantId = intent.getStringExtra(KEY_PARTICIPANT_ID)
        mParticipant = intent.getSerializableExtra(KEY_PARTICIPANT) as? Participant
        val participantId: String? = mParticipantId
        if (participantId == null) {
            finish()
            return
        }
        supportActionBar?.title = mParticipant?.displayName ?: mParticipantId
        listMessages.layoutManager = LinearLayoutManager(this).apply { reverseLayout = true }
        listMessages.adapter = PagedMessageListAdapter()
        Cherry.Messaging.tryPublishingMessages(this)
        fabSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun cancelNotification(notificationId: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    override fun onResume() {
        super.onResume()
        val participantId = mParticipantId
        if (participantId != null) {
            val notificationId = CherryChatApplication.idFor(participantId)
            cancelNotification(notificationId)
            Cherry.Messaging.markAsRead(this, participantId)
        }
        observeMessages()
    }

    private fun observeMessages() {
        val participantId = mParticipantId ?: return
        val liveData = Cherry.Messaging.getMessageLiveDataForConversation(this, participantId).create(0, 30)
        (listMessages.adapter as PagedMessageListAdapter).setList(liveData.value)
        liveData.observe({lifecycle}, { pagedList ->
            (listMessages.adapter as PagedMessageListAdapter).setList(pagedList)
            listMessages.smoothScrollToPosition(0)
        })
    }

    private fun sendMessage() {
        val text = etComposeView.text.toString()
        val recipientId = mParticipantId ?: return
        if (text.isNotBlank()) {
            Cherry.Messaging.queueTextMessage(this, text, recipientId, {
                Cherry.Messaging.tryPublishingMessages(this)
            })
            etComposeView.setText("")
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(Cherry.ACTION_NEW_INCOMING_MESSAGE)
        filter.priority = 10
        registerReceiver(mBroadcastReceiver, filter, Cherry.PERMISSION_RECEIVE_MESSAGES, Handler())
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mBroadcastReceiver)
    }
}