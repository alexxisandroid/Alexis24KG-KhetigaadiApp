package com.khetigaadi.isOwner
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.MyResponse
data class OwnerTractorResp(
    @SerializedName("data")
    val ownerTractorData: OwnerTractorData,
) : MyResponse()