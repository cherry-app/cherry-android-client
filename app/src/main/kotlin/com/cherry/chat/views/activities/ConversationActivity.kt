package com.cherry.chat.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cherry.chat.R
import com.cherry.chat.views.adapters.PagedMessageListAdapter
import com.cherry.core.Cherry
import com.cherry.core.models.Participant
import kotlinx.android.synthetic.main.activity_conversation.*

/**
 * Created by girish on 11/21/17.
 */

class ConversationActivity : AppCompatActivity() {

    companion object {
        const val KEY_PARTICIPANT_ID = "participantId"
        const val KEY_PARTICIPANT = "participant"
    }



    private var mParticipant: Participant? = null
    private var mParticipantId: String? = null

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
        Cherry.Messaging.markAsRead(participantId)
        supportActionBar?.title = mParticipant?.displayName ?: mParticipantId
        listMessages.layoutManager = LinearLayoutManager(this).apply { reverseLayout = true }
        listMessages.adapter = PagedMessageListAdapter()
        Cherry.Messaging.tryPublishingMessages(this)
        fabSend.setOnClickListener {
            sendMessage()
        }
    }

    override fun onResume() {
        super.onResume()
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

}