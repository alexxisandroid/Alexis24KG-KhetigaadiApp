package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class CompImpRespData(
    @SerializedName("first_implement")
    val firstImplement: List<CompImplement>,
    @SerializedName("implement_spec_level")
    val implementSpecLevel: List<ImplementSpecLevel>,
    @SerializedName("secound_implement")
    val secondImplement: List<CompImplement>
)