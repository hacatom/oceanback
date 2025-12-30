package com.github.ityeri.oceanwiki.core.api.post.dto

import com.github.ityeri.oceanwiki.core.location.Location
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: ULong,
    val authorId: ULong,
    val title: String,
    val content: String,
    val location: Location, // Embed the full location details
    val category: Int,
    val mediaIds: List<ULong>, // NEW: List of media IDs attached to this post
    val upvoteCount: Int,
    val downvoteCount: Int,
    val createdAt: String, // String representation of datetime
    val updatedAt: String  // String representation of datetime
)
