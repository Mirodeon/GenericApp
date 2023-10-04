package com.mirodeon.genericapp.network.dto

import com.google.gson.annotations.SerializedName

data class DataWaifuDto(
    @SerializedName("images")
    var images: List<WaifuDto>
)

data class WaifuDto(
    @SerializedName("source")
    var source: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("tags")
    var tags: List<TagDto>
)

data class TagDto(
    @SerializedName("name")
    var name: String?
)
