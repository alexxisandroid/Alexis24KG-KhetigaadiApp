package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class ImplementSpecLevel(
    @SerializedName("implement_spec_id")
    val implementSpecId: Int,
    @SerializedName("implement_spec_name")
    val implementSpecName: String
)