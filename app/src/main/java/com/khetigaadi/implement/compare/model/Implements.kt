package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class Implements(
    @SerializedName("first_implement")
    val firstImplement: Implement,
    @SerializedName("secound_implement")
    val secondImplement: Implement
)