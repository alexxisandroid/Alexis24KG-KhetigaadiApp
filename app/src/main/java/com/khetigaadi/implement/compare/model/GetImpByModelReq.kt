package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class GetImpByModelReq(
    @SerializedName("implements")
    val implements: ImplementsInfo
)