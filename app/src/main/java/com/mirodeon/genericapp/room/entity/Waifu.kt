package com.mirodeon.genericapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(indices = [Index(value = ["url"], unique = true)])
data class Waifu(
    @PrimaryKey(autoGenerate = true)
    val waifuId: Long = 0,
    @ColumnInfo(name = "source")
    var source: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "isFav")
    val isFav: Boolean = false
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long = 0,
    @ColumnInfo(name = "name")
    val name: String
)

@Entity(primaryKeys = ["waifuId", "tagId"])
data class WaifuTagCrossRef(
    val waifuId: Long,
    val tagId: Long
)

data class WaifuWithTag(
    @Embedded
    val waifu: Waifu,
    @Relation(
        parentColumn = "waifuId",
        entityColumn = "tagId",
        associateBy = Junction(WaifuTagCrossRef::class)
    )
    val tags: List<Tag>
)

data class TagWithWaifu(
    @Embedded
    val tag: Tag,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "waifuId",
        associateBy = Junction(WaifuTagCrossRef::class)
    )
    val books: List<Waifu>
)