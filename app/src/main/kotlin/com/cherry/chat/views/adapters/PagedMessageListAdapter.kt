package com.cherry.chat.views.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cherry.chat.R
import com.cherry.core.models.Message
import com.cherry.core.models.MessageState

/**
 * Created by girish on 11/22/17.
 */

class PagedMessageListAdapter: PagedListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        const val VIEW_TYPE_INCOMING = 1
        const val VIEW_TYPE_OUTGOING = 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder ?: return
        val message = getItem(position)
        if (holder is OutgoingMessageViewHolder) {
            holder.tvContent.text = message?.content
        }
        if (holder is IncomingMessageViewHolder) {
            holder.tvContent.text = message?.content
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position) ?: return 0
        return if (message.state == MessageState.RECEIVED) {
            VIEW_TYPE_INCOMING
        } else {
            VIEW_TYPE_OUTGOING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        parent ?: return null
        return when (viewType) {
            VIEW_TYPE_INCOMING -> IncomingMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_incoming_message, parent, false))
            VIEW_TYPE_OUTGOING -> OutgoingMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_outgoing_message, parent, false))
            else -> null
        }
    }

    class OutgoingMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvContent = itemView.findViewById<TextView>(R.id.tvOutgoingMessageText) as TextView
    }

    class IncomingMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvContent = itemView.findViewById<TextView>(R.id.tvIncomingMessageText) as TextView
    }
}