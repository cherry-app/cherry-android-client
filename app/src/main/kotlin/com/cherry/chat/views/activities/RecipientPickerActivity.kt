package com.cherry.chat.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cherry.chat.R
import com.cherry.chat.views.adapters.ParticipantListAdapter
import com.cherry.core.Cherry
import com.cherry.core.models.Participant
import kotlinx.android.synthetic.main.activity_contact_list.*

/**
 * Created by girish on 11/21/17.
 */

class RecipientPickerActivity : AppCompatActivity() {

    companion object {
        const val KEY_PARTICIPANT = "participant"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        setResult(Activity.RESULT_CANCELED)
        Cherry.Contacts.sync(this)
        listParticipants.layoutManager = LinearLayoutManager(this)
        listParticipants.adapter = ParticipantListAdapter().apply { onParticipantSelected = { participant ->  onParticipantSelected(participant)} }
        observeParticipants()
    }

    private fun onParticipantSelected(participant: Participant) {
        val data = Intent()
        data.putExtra(KEY_PARTICIPANT, participant)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun observeParticipants() {
        val liveData = Cherry.Contacts.getParticipantsLiveData(this)
        (listParticipants.adapter as ParticipantListAdapter).setList(liveData.value)
        listParticipants.adapter.notifyDataSetChanged()
        liveData.observeForever { participants ->
            participants ?: return@observeForever
            (listParticipants.adapter as ParticipantListAdapter).updateList(participants)
        }
    }

}