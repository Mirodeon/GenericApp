package com.mirodeon.genericapp.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mirodeon.genericapp.room.converter.Converters
import com.mirodeon.genericapp.room.dao.TagDao
import com.mirodeon.genericapp.room.dao.WaifuDao
import com.mirodeon.genericapp.room.entity.Tag
import com.mirodeon.genericapp.room.entity.Waifu
import com.mirodeon.genericapp.room.entity.WaifuTagCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Waifu::class, Tag::class, WaifuTagCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class WaifuDatabase : RoomDatabase() {

    abstract fun waifuDao(): WaifuDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: WaifuDatabase? = null

        fun getDatabase(context: Context): WaifuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    WaifuDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
