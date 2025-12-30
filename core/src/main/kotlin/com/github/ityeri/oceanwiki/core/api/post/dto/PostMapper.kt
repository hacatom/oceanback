package com.github.ityeri.oceanwiki.core.api.post.dto

import com.github.ityeri.oceanwiki.core.location.Location
import com.github.ityeri.oceanwiki.core.location.Point
import com.github.ityeri.oceanwiki.core.post.PostRow

// Helper function to map from the DB entity to the API response DTO
fun PostRow.toResponse(): PostResponse {
    val locationRow = this.location

    return PostResponse(
        id = this.id.value,
        authorId = this.author.id.value,
        title = this.title,
        content = this.content,
        location = Location(
            point = Point(locationRow.latitude, locationRow.longitude),
            fullAddress = locationRow.fullAddress,
            province = locationRow.province,
            city = locationRow.city
            // Removed segment assignment
        ),
        category = this.category,
        mediaIds = this.medias.map { it.id.value }, // Populate media IDs
        upvoteCount = this.upvoteCount,
        downvoteCount = this.downvoteCount,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}
