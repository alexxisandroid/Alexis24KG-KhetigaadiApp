package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.Brand
import com.khetigaadi.implement.newImplement.util.Type
data class ImplementsInfo(
    @SerializedName("brands")
    val brands: Brand,
    @SerializedName("model")
    val model: ImplementModel,
    @SerializedName("type")
    val type: Type
)