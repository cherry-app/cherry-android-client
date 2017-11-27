package com.cherry.chat.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cherry.chat.R
import com.cherry.chat.views.adapters.ParticipantListAdapter
import com.cherry.chat.views.base.SessionActivity
import com.cherry.core.Cherry
import com.cherry.core.models.Participant
import kotlinx.android.synthetic.main.activity_contact_list.*

/**
 * Created by girish on 11/21/17.
 */

class RecipientPickerActivity : SessionActivity() {

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
    }

    override fun onResume() {
        super.onResume()
        observeParticipants()
    }

    private fun onParticipantSelected(participant: Participant) {
        val data = Intent()
        data.putExtra(KEY_PARTICIPANT, participant)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Cherry.Contacts.getParticipantsLiveData(this).removeObservers(this)
    }

    private fun observeParticipants() {
        val liveData = Cherry.Contacts.getParticipantsLiveData(this)
        (listParticipants.adapter as ParticipantListAdapter).setList(liveData.value)
        listParticipants.adapter.notifyDataSetChanged()
        liveData.observe( { lifecycle}, { participants ->
            participants ?: return@observe
            (listParticipants.adapter as ParticipantListAdapter).updateList(participants)
        })
    }

}