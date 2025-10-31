package com.khetigaadi.isOwner


import com.google.gson.annotations.SerializedName

data class OwnerTractorData(
    @SerializedName("brand_id")
    val brandId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("tractor_model_id")
    val tractorModelId: Int,
    @SerializedName("tractor_month")
    val tractorMonth: String,
    @SerializedName("tractor_year")
    val tractorYear: Int,
    @SerializedName("user_id")
    val userId: Int
)