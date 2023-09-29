package com.mirodeon.genericapp.utils.sharedPref

import android.app.Activity
import android.content.Context

class SharedPrefManager(activity: Activity) {
    companion object {
        private const val PREFERENCE_KEY = "com.mirodeon.exogeoloc"
    }

    enum class KeyPref(val value: String) {
        FIRST_TIME_OPENING("FIRST_TIME_OPENING")
    }

    val sharedPref =
        activity.applicationContext.getSharedPreferences(
            PREFERENCE_KEY,
            Context.MODE_PRIVATE
        )

    val editor = sharedPref.edit()
}