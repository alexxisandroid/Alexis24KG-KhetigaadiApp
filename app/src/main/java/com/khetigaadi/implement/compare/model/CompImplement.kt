package com.khetigaadi.implement.compare.model


import com.google.gson.annotations.SerializedName

class CompImplement() {
    @SerializedName("average_rating")
    val averageRating: Double= 0.0

    @SerializedName("brand_id")
    val brandId: Int=0

    @SerializedName("implement_id")
    val implementId: Int=0

    @SerializedName("implement_image")
    val implementImage: ImplementImage?=null

    @SerializedName("implement_model")
    val implementModel: ImplementModel?=null

    @SerializedName("implement_model_id")
    val implementModelId: Int=0

    @SerializedName("implement_name")
    val implementName: String?=null

    @SerializedName("implement_spec_data")
    val implementSpecData: List<ImplementSpecData>?=null

    @SerializedName("implement_type")
    val implementType: ImplementType?=null

    @SerializedName("implement_type_id")
    val implementTypeId: Int = 0

    @SerializedName("is_bookmark")
    val isBookmark: Boolean = false

    @SerializedName("review")
    val review: List<Review>? = null
}