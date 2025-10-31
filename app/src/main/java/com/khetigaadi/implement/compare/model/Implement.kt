package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class Implement(
    @SerializedName("brand_id")
    val brandId: String,
    @SerializedName("model_id")
    val modelId: String,
    @SerializedName("Type_id")
    val typeId: String)