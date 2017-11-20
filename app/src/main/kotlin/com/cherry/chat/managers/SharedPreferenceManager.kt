package com.cherry.chat.managers

import android.content.Context

/**
 * Created by kavins on 11/19/17.
 */

object SharedPreferenceManager {

    private const val CHERRY_CHAT_PREFS = "com.cherry.chat.prefs"

    private const val KEY_PHONE_NUMBER = "phoneNumber"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_HAS_ASKED_CONTACTS_PERMISSION = "hasAskedContactsPermission"

    fun setPhoneNumber(context: Context, phoneNumber: String) {
        context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE).edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply()
    }

    fun getPhoneNumber(context: Context): String? =
            context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE).getString(KEY_PHONE_NUMBER, null)

    fun setUserName(context: Context, userName: String) {
        context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE).edit().putString(KEY_USER_NAME, userName).apply()
    }

    fun getUserName(context: Context): String? =
            context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE).getString(KEY_USER_NAME, null)

    fun setHasAskedContactsPermission(context: Context, hasAsked: Boolean) {
        context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE).edit().putBoolean(KEY_HAS_ASKED_CONTACTS_PERMISSION, hasAsked).apply()
    }

    fun getHasAskedContactsPermission(context: Context): Boolean =
            context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE).getBoolean(KEY_HAS_ASKED_CONTACTS_PERMISSION, false)
}