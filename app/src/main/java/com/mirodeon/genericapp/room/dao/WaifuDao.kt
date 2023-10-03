package com.mirodeon.genericapp.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mirodeon.genericapp.room.entity.Tag
import com.mirodeon.genericapp.room.entity.TagWithWaifu
import com.mirodeon.genericapp.room.entity.Waifu
import com.mirodeon.genericapp.room.entity.WaifuTagCrossRef
import com.mirodeon.genericapp.room.entity.WaifuWithTag
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuDao {
    @Transaction
    @Query("SELECT * FROM waifu")
    fun getAll(): Flow<List<WaifuWithTag>>

    @Transaction
    @Query("SELECT * FROM waifu WHERE waifuId = :id")
    fun findById(id: Long): WaifuWithTag

    @Transaction
    @Query("SELECT * FROM waifu WHERE url = :url LIMIT 1")
    fun findByUrl(url: String): WaifuWithTag

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(waifu: Waifu): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertRef(ref: WaifuTagCrossRef)

    @Delete
    fun deleteRef(ref: WaifuTagCrossRef)

    fun insertAll(vararg waifus: WaifuWithTag) {
        for (waifu in waifus) {
            val waifuId = insert(waifu.waifu)
            if (waifu.tags.isNotEmpty()) {
                for (tag in waifu.tags) {
                    insertRef(WaifuTagCrossRef(waifuId = waifuId, tagId = tag.tagId))
                }
            }
        }
    }

    fun deleteAll(vararg waifus: WaifuWithTag) {
        for (waifu in waifus) {
            if (waifu.tags.isNotEmpty()) {
                for (tag in waifu.tags) {
                    deleteRef(WaifuTagCrossRef(waifuId = waifu.waifu.waifuId, tagId = tag.tagId))
                }
            }
            delete(waifu.waifu)
        }
    }

    @Delete
    fun delete(waifu: Waifu)

    @Query("UPDATE waifu SET isFav=:isFav WHERE waifuId = :id")
    fun isFav(id: Long, isFav: Boolean)
}

@Dao
interface TagDao {
    @Query("SELECT * FROM tag")
    fun getAll(): Flow<List<Tag>>

    @Transaction
    @Query("SELECT * FROM tag")
    fun getAllWithBooks(): Flow<List<TagWithWaifu>>

    @Query("SELECT * FROM tag WHERE name = :name")
    fun findByName(name: String): Tag

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(genre: Tag): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg tags: Tag)
}