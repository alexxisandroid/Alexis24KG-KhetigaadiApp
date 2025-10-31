package com.khetigaadi.implement.compare.model
import com.google.gson.annotations.SerializedName
data class Review(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("implement_id")
    val implementId: Int,
    @SerializedName("is_default")
    val isDefault: Int,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("review_id")
    val reviewId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)