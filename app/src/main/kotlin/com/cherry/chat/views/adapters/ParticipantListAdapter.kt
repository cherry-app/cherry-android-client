package com.cherry.chat.views.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cherry.chat.R
import com.cherry.core.models.Participant
import com.cherry.core.models.RecipientType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by girish on 11/22/17.
 */

class ParticipantListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val participants: MutableList<Participant> = ArrayList()

    var onParticipantSelected: ((Participant) -> Unit)? = null

    override fun getItemCount(): Int = participants.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder ?: return
        val participant = participants[position]
        if (holder is ParticipantViewHolder) {
            holder.tvName.text = participant.displayName
            holder.tvType.text = getTypeLabel(participant.type)
            holder.itemView.setOnClickListener {
                onParticipantSelected?.invoke(participant)
            }
        }
    }

    private fun getTypeLabel(type: RecipientType): String = when(type) {
        RecipientType.SELF -> "YOU"
        RecipientType.INDIVIDUAL -> "Your contact"
        RecipientType.GROUP -> "A group you're in"
        RecipientType.UNKNOWN -> "A wild Pokemon has appeared!"
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        parent ?: return null
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_participant, parent, false)
        return ParticipantViewHolder(view)
    }

    class ParticipantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvParticipantName) as TextView
        val tvType = itemView.findViewById<TextView>(R.id.tvParticipantType) as TextView
        val ivPicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture) as ImageView
    }

    fun setList(participants: List<Participant>?) {
        participants ?: return
        this.participants.addAll(participants)
    }

    fun updateList(participants: List<Participant>?) {
        participants ?: return
        Observable.fromCallable { DiffUtil.calculateDiff(DiffUtilCalculator(this.participants, participants)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    this.participants.clear()
                    this.participants.addAll(participants)
                    result.dispatchUpdatesTo(this@ParticipantListAdapter)
                }
    }

    class DiffUtilCalculator(private val oldParticipants: List<Participant>, private val newParticipants: List<Participant>): DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldParticipants[oldItemPosition].id == newParticipants[newItemPosition].id

        override fun getOldListSize(): Int = oldParticipants.size

        override fun getNewListSize(): Int = newParticipants.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldParticipants[oldItemPosition].displayName == newParticipants[newItemPosition].displayName
    }
}