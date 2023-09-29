package com.mirodeon.genericapp.network.dto

import com.google.gson.annotations.SerializedName

data class ItemFromSecondApi(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("propertyFromSecondApi")
    var propertyFromSecondApi: String? = null
)