package com.cherry.chat.managers

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by kavins on 11/19/17.
 */

object SharedPreferenceManager {

    private const val CHERRY_CHAT_PREFS = "com.cherry.chat.prefs"

    private const val KEY_PHONE_NUMBER = "phoneNumber"
    private const val KEY_USER_NAME = "userName"

    @Volatile private var SHARED_PREFERENCE_INSTANCE: SharedPreferences? = null

    fun initPreference(context: Context) {
        SHARED_PREFERENCE_INSTANCE = context.getSharedPreferences(CHERRY_CHAT_PREFS, Context.MODE_PRIVATE)
    }

    fun setPhoneNumber(phoneNumber: String) {
        SHARED_PREFERENCE_INSTANCE?.edit()?.putString(KEY_PHONE_NUMBER, phoneNumber)?.apply()
    }

    fun getPhoneNumber(): String? {
        return SHARED_PREFERENCE_INSTANCE?.getString(KEY_PHONE_NUMBER, null)
    }

    fun setUserName(userName: String) {
        SHARED_PREFERENCE_INSTANCE?.edit()?.putString(KEY_USER_NAME, userName)?.apply()
    }

    fun getUserName(): String? {
        return SHARED_PREFERENCE_INSTANCE?.getString(KEY_USER_NAME, null)
    }
}