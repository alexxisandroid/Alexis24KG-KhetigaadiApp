package com.khetigaadi.tractor.rent.rentTractorNewUi.model
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.Brand
import com.khetigaadi.base.model.Model
data class UploadRentTrReq(
    @SerializedName("brand")
    val brand: Brand,
    @SerializedName("hours")
    val hours: String,
   /* @SerializedName("implement_type_list")
    val implementTypeList: List<ImplementType>,*/
    @SerializedName("note")
    val note: String,
    @SerializedName("radious")
    val radious: Int,
    @SerializedName("tractor_model")
    val tractorModel: Model,
    @SerializedName("tractor_month")
    val tractorMonth: String,
    @SerializedName("tractor_year")
    val tractorYear: String
)