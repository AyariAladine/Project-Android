package com.example.bookproject.DataClass
data class Story(
    val id: String,
    val title: String,
    val content : String,
    val imageUrl: String,
    val likeCount: Int,
    val liked: Boolean
)
