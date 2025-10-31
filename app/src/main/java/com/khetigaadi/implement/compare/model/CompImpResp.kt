package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.implement.newImplement.model.Others
data class CompImpResp(
    @SerializedName("data")
    val `data`: CompImpRespData,
    @SerializedName("others")
    val others: Others,
):MyResponse()