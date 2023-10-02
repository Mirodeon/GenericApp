package com.mirodeon.genericapp.utils.sharedPref

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(activity: Activity) {
    companion object {
        private const val PREFERENCE_KEY = "com.mirodeon.genericapp"
    }

    enum class KeyPref(val value: String) {
        FIRST_TIME_OPENING("FIRST_TIME_OPENING"),
        LOGIN_KEY( "LOGIN_KEY"),
        PASSWORD_KEY ("PASSWORD_KEY")
    }

    val sharedPref: SharedPreferences =
        activity.applicationContext.getSharedPreferences(
            PREFERENCE_KEY,
            Context.MODE_PRIVATE
        )

    val editor: SharedPreferences.Editor = sharedPref.edit()
}