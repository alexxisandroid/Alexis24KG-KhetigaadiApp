package com.khetigaadi.tractor.newTractor.model
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.MyResponse
data class NewTrSearchByNameResp(
    @SerializedName("data")
    val `data`: List<NewTractor>,
) : MyResponse()