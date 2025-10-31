package com.khetigaadi.user.myUsedTractor.model
import com.google.gson.annotations.SerializedName
data class MyUploadedTrReq(
    @SerializedName("filter")
    val filter: Filter
)