package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class ImplementSpecData(
    @SerializedName("imp_spec_data_id")
    val impSpecDataId: Int,
    @SerializedName("implement_harvester_specification_label")
    val implementHarvesterSpecificationLabel: ImplementHarvesterSpecificationLabel,
    @SerializedName("implement_id")
    val implementId: Int,
    @SerializedName("implement_spec_id")
    val implementSpecId: Int,
    @SerializedName("is_top")
    val isTop: Int,
    @SerializedName("position")
    val position: Int,
    @SerializedName("spec_data")
    val specData: String
)