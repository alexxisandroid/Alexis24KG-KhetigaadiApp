package com.khetigaadi.user.myUsedTractor.model
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.MyResponse
data class InterestedBuyersResp(
    @SerializedName("data")
    val `data`: List<InterestedBuyersData>
) : MyResponse()