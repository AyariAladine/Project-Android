package com.example.bookproject.DataClass

import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("_id") val id: String,
    val title: String,
    val content : String,
    val imageUrl: String,
    val likeCount: Int,
    val liked: Boolean
)
data class StoryRequest(val prompt: String)

data class StoryResponse(
    val story: Story?,
    val error: String?
)
