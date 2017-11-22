package com.cherry.chat.views.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cherry.chat.R
import com.cherry.core.models.Conversation
import com.cherry.core.models.ConversationWithParticipant
import com.cherry.core.models.Participant
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by girish on 11/22/17.
 */

class ConversationListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val conversations: MutableList<ConversationWithParticipant> = ArrayList()

    var onConversationSelected: ((Conversation, Participant?) -> Unit)? = null

    override fun getItemCount(): Int = conversations.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder ?: return
        val conversation = conversations[position].conversation ?: return
        val participant = conversations[position].participants?.get(0)
        if (holder is ParticipantViewHolder) {
            holder.tvName.text = participant?.displayName ?: conversation.participantId
            holder.tvSnippet.text = conversation.snippet
            holder.itemView.setOnClickListener {
                onConversationSelected?.invoke(conversation, participant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        parent ?: return null
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_conversation, parent, false)
        return ParticipantViewHolder(view)
    }

    class ParticipantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvConversationName) as TextView
        val tvSnippet = itemView.findViewById<TextView>(R.id.tvSnippet) as TextView
        val ivPicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture) as ImageView
    }

    fun setList(conversations: List<ConversationWithParticipant>?) {
        conversations ?: return
        this.conversations.addAll(conversations)
    }

    fun updateList(conversations: List<ConversationWithParticipant>?) {
        conversations ?: return
        Observable.fromCallable { DiffUtil.calculateDiff(DiffUtilCalculator(this.conversations, conversations)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    this.conversations.clear()
                    this.conversations.addAll(conversations)
                    result.dispatchUpdatesTo(this@ConversationListAdapter)
                }
    }

    class DiffUtilCalculator(private val oldConversations: List<ConversationWithParticipant>, private val newConversations: List<ConversationWithParticipant>): DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldConversations[oldItemPosition].conversation?.snippet == newConversations[newItemPosition].conversation?.snippet

        override fun getOldListSize(): Int = oldConversations.size

        override fun getNewListSize(): Int = newConversations.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldConversations[oldItemPosition].conversation?.snippet == newConversations[newItemPosition].conversation?.snippet
    }
}