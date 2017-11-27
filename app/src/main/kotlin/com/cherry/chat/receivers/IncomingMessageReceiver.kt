package com.cherry.chat.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.cherry.chat.CherryChatApplication
import com.cherry.chat.R
import com.cherry.chat.views.activities.ConversationActivity
import com.cherry.core.Cherry
import com.cherry.core.models.Message
import com.cherry.core.models.ParticipantWithMessages

/**
 * Created by girish on 11/27/17.
 */

class IncomingMessageReceiver: BroadcastReceiver() {

    companion object {
        const val incomingMessageNotificationId = "incomingMessages"
        const val notificationId = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        if (intent.action == Cherry.ACTION_NEW_INCOMING_MESSAGE) {
            val message = intent.getSerializableExtra(Cherry.KEY_MESSAGE) as? Message
            message ?: return
            Cherry.Messaging.getUnreadMessages(context, { unreadMessages ->
                unreadMessages ?: return@getUnreadMessages
                showNotification(context, unreadMessages)
            })
        }
    }

    private fun showNotification(context: Context, unreadMessages: List<ParticipantWithMessages>) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(incomingMessageNotificationId, "Message notifications", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Messages that are sent by your friends and contacts"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        unreadMessages.forEach { participantWithMessages ->
            val participant = participantWithMessages.participant ?: return@forEach
            val messages = participantWithMessages.messages ?: return@forEach
            if (messages.isEmpty()) {
                return@forEach
            }
            val style = NotificationCompat.MessagingStyle("Me")

            messages.forEach { message ->
                style.addMessage(message.content, message.receivedTime, participant.displayName)
            }

            val id = CherryChatApplication.idFor(participant.id)
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(ConversationActivity.KEY_PARTICIPANT_ID, participant.id)
            intent.putExtra(ConversationActivity.KEY_PARTICIPANT, participant)
            intent.action = "" + System.currentTimeMillis()
            val pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT)

            val notification = NotificationCompat.Builder(context, incomingMessageNotificationId)
                    .setContentTitle("Cherry")
                    .setContentText("You have new messages")
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setGroup("incomingMessages")
                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setStyle(style)
                    .build()

            notificationManager.notify(id, notification)
        }
    }
}