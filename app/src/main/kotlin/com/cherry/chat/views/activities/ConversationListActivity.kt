package com.cherry.chat.views.activities

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cherry.chat.R
import com.cherry.chat.managers.SharedPreferenceManager
import com.cherry.chat.viewmodels.ConversationViewModel
import com.cherry.chat.views.adapters.ConversationListAdapter
import com.cherry.chat.views.base.SessionActivity
import com.cherry.core.Cherry
import com.cherry.core.models.Conversation
import com.cherry.core.models.Participant
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_conversation_list.*

/**
 * Created by girish on 11/19/17.
 */

class ConversationListActivity: SessionActivity() {

    companion object {
        const val REQUEST_CODE_CONTACTS_PERMISSION = 701
        const val REQUEST_CODE_PARTICIPANT_PICKER = 101
    }

    var conversationViewModel: ConversationViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)
        Cherry.Messaging.tryPublishingMessages(this)
        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel::class.java)
        syncIfPossible()
        listConversations.layoutManager = LinearLayoutManager(this)
        listConversations.adapter = ConversationListAdapter().apply { onConversationSelected = { conversation, participant -> this@ConversationListActivity.onConversationSelected(conversation, participant) } }
        observeConversations()
        fabNewMessage.setOnClickListener {
            val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    showPermissionRationale()
                } else {
                    if (SharedPreferenceManager.getHasAskedContactsPermission(this)) {
                        showPermissionRationaleBlocked()
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_CONTACTS_PERMISSION)
                    }
                }
            } else {
                showContactsPicker()
            }
        }

        val firebaseToken = FirebaseInstanceId.getInstance().token ?: return
        Log.d("CherryFCM", "Found existing token: $firebaseToken")
        Cherry.Session.updateFirebaseToken(firebaseToken, { success ->
            // TODO If not successful, need to handle retry logic
            Log.d("CherryFCM", "Token sync: $success")
        })
    }

    private fun syncIfPossible() {
        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            if (!Cherry.Contacts.isSyncing) {
                Cherry.Contacts.sync(this)
            }
        }
    }

    private fun onConversationSelected(conversation: Conversation, participant: Participant?) {
        showConversationFor(conversation.participantId, participant)
    }

    private fun observeConversations() {
        val liveData = conversationViewModel?.getConversationLiveData(this)
        liveData ?: return
        (listConversations.adapter as ConversationListAdapter).setList(liveData.value)
        liveData.observeForever { list ->
            Log.d("CherryObserver", "Found change")
            (listConversations.adapter as ConversationListAdapter).updateList(list)
            if (list != null && list.isNotEmpty()) {
                emptyView.visibility = View.GONE
            } else {
                emptyView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_conversation_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionLogout -> {
                logoutAndLaunchSignUp(true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        val liveData = Cherry.Messaging.getConversationLiveData(this)
        liveData.removeObservers(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_CODE_CONTACTS_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showContactsPicker()
                } else {
                    Snackbar.make(listConversations, "Contacts permission was denied. Cannot compose a message.", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_PARTICIPANT_PICKER -> {
                if (resultCode == Activity.RESULT_OK) {
                    val participant = data?.getSerializableExtra(RecipientPickerActivity.KEY_PARTICIPANT) as? Participant
                    participant ?: return
                    showConversationFor(participant.id, participant)
                } else {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showConversationFor(participantId: String, participant: Participant?) {
        startActivity(Intent(this, ConversationActivity::class.java).apply {
            putExtra(ConversationActivity.KEY_PARTICIPANT_ID, participantId)
            putExtra(ConversationActivity.KEY_PARTICIPANT, participant)
        })
    }

    private fun showContactsPicker() {
        startActivityForResult(Intent(this, RecipientPickerActivity::class.java), REQUEST_CODE_PARTICIPANT_PICKER)
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
                .setMessage("Cherry Chat needs to access your contacts in order to send messages. Please click on 'Allow' when we ask for the permission.")
                .setPositiveButton("OK", { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_CONTACTS_PERMISSION)
                }).setTitle("Permission needed")
                .show()
    }

    private fun showPermissionRationaleBlocked() {
        AlertDialog.Builder(this)
                .setMessage("Cherry Chat needs to access your contacts in order to send messages. Please go to app settings and allow this permission.")
                .setPositiveButton("OK", { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_CONTACTS_PERMISSION)
                }).setTitle("Permission needed")
                .show()
    }
}