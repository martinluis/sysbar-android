package com.lcmm.sysbar.android.services

import android.content.Context
import android.content.SharedPreferences

class LocalStorageService(context: Context) {

    private val activeUserKey = "active_user_key"

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    /**
     *
     */
    fun setActiveUser(username: String) {
        val editor = prefs.edit()
        editor.putString(activeUserKey, username)
        editor.apply()
    }

    /**
     *
     */
    fun getActiveUser(): String? {
        return prefs.getString(activeUserKey,"")
    }

    /**
     *
     */
    fun removeActiveUser() {
        val editor = prefs.edit()
        editor.remove(activeUserKey)
        editor.apply()
    }

    /**
     *
     */
    fun isLoggedIn(): Boolean {
        return !getActiveUser().isNullOrEmpty()
    }


}