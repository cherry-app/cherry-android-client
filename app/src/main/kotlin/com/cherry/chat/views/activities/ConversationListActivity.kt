package com.cherry.chat.views.activities

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cherry.chat.R
import com.cherry.chat.managers.SharedPreferenceManager
import com.cherry.chat.viewmodels.ConversationViewModel
import com.cherry.core.Cherry
import kotlinx.android.synthetic.main.activity_conversation_list.*

/**
 * Created by girish on 11/19/17.
 */

class ConversationListActivity: AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_CONTACTS_PERMISSION = 701
    }

    lateinit var conversationViewModel: ConversationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)
        syncIfPossible()
        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel::class.java)
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
    }

    private fun syncIfPossible() {
        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            if (!Cherry.Contacts.isSyncing) {
                Cherry.Contacts.sync(this)
            }
        }
    }

    private fun observeConversations() {
        conversationViewModel.getConversationLiveData(this).observeForever { list ->
            Log.d("CherryChat", "Conversation changed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        conversationViewModel.getConversationLiveData(this).removeObservers(this)
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

    private fun showContactsPicker() {

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