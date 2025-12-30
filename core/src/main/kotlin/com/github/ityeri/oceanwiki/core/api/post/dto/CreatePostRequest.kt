package com.github.ityeri.oceanwiki.core.api.post.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val latitude: Double,
    val longitude: Double,
    val category: Int,
    val title: String,
    val content: String
)
