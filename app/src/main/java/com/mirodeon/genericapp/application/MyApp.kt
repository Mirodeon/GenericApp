package com.mirodeon.genericapp.application

import android.app.Application
import com.mirodeon.genericapp.room.db.WaifuDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    val database: WaifuDatabase by lazy { WaifuDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}