package com.khetigaadi.user.myUsedTractor.model
import com.google.gson.annotations.SerializedName
data class Filter(
    @SerializedName("status")
    val status: String)