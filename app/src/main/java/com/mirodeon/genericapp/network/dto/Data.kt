package com.mirodeon.genericapp.network.dto

import com.google.gson.annotations.SerializedName

data class Data<T>(
    @SerializedName("data")
    var data: T
)