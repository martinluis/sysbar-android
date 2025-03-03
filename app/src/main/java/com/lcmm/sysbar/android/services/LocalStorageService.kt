package com.lcmm.sysbar.android.services

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.lcmm.sysbar.android.models.User

class LocalStorageService(context: Context) {

    private val activeUserKey = "active_user_key"

    private val prefs: SharedPreferences = context.getSharedPreferences("sysbar_prefs", Context.MODE_PRIVATE)

    /**
     *
     */
    fun setActiveUser(user: User) {
        val editor = prefs.edit()
        val userJson = Gson().toJson(user)
        editor.putString(activeUserKey, userJson)
        editor.apply()
    }

    /**
     *
     */
    fun getActiveUser(): User? {
        val userJson = prefs.getString(activeUserKey, null)

        // If the JSON string exists, deserialize it to a User object
        return if (userJson != null) {
            Gson().fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

}