package com.mirodeon.genericapp.network.dto

import com.google.gson.annotations.SerializedName

data class ItemFromFirstApi(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("propertyFromFirstApi")
    var propertyFromFirstApi: String? = null
)