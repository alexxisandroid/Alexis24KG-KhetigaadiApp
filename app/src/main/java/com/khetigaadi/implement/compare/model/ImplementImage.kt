package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class ImplementImage(
    @SerializedName("implement_id")
    val implementId: Int,
    @SerializedName("implement_image")
    val implementImage: String,
    @SerializedName("implement_image_id")
    val implementImageId: Int)