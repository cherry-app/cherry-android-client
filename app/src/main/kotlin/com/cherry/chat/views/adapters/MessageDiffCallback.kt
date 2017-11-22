package com.cherry.chat.views.adapters

import android.support.v7.recyclerview.extensions.DiffCallback
import com.cherry.core.models.Message

/**
 * Created by girish on 11/22/17.
 */

class MessageDiffCallback: DiffCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.content == newItem.content

}