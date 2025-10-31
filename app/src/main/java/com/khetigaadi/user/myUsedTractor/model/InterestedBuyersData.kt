package com.khetigaadi.user.myUsedTractor.model
import com.google.gson.annotations.SerializedName
import com.khetigaadi.user.model.User
data class InterestedBuyersData(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("user")
    val user: User)