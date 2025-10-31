package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class ImplementType(
    @SerializedName("implement_category_id")
    val implementCategoryId: Int,
    @SerializedName("implement_type_id")
    val implementTypeId: Int,
    @SerializedName("implement_type_image")
    val implementTypeImage: String,
    @SerializedName("implement_type_name")
    val implementTypeName: String
)