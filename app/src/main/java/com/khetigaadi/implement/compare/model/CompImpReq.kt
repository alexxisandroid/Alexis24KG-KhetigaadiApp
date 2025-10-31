package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class CompImpReq(
    @SerializedName("implements")
    val implements: Implements
)