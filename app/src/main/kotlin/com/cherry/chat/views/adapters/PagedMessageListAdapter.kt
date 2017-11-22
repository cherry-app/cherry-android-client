package com.cherry.chat.views.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cherry.chat.R
import com.cherry.core.models.Message

/**
 * Created by girish on 11/22/17.
 */

class PagedMessageListAdapter: PagedListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder ?: return
        val message = getItem(position)
        if (holder is MessageViewHolder) {
            holder.tvContent.text = message?.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        parent ?: return null
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_outgoing_message, parent, false))
    }

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvContent = itemView.findViewById<TextView>(R.id.tvOutgoingMessageText) as TextView
    }
}