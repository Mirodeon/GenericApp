package com.mirodeon.genericapp.network.dto

import com.google.gson.annotations.SerializedName

data class ItemFromSecondApi(
    @SerializedName("priceUsd")
    var priceUsd: String? = null,
    @SerializedName("time")
    var time: Long? = null
)