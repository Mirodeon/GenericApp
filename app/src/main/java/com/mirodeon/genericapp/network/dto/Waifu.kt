package com.mirodeon.genericapp.network.dto

import com.google.gson.annotations.SerializedName

data class DataWaifu(
    @SerializedName("images")
    var images: List<Waifu>
)

data class Waifu(
    @SerializedName("source")
    var source: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("tags")
    var tags: List<Tag>
)

data class Tag(
    @SerializedName("name")
    var name: String?
)
