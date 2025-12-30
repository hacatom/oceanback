package com.github.ityeri.oceanwiki.core.api.media

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.contentType
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.http.HttpHeaders
import io.ktor.server.response.header

fun Routing.mediaController(mediaService: MediaService) {
    post("/posts/{post_id}/media") { // Changed route
        val postId = call.parameters["post_id"]?.toULongOrNull()
        if (postId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid post ID")
            return@post
        }

        val channel = call.receiveChannel()
        val contentType = call.request.contentType().toString()

        val newMedia = mediaService.saveMedia(channel, contentType, postId) // Pass postId
        if (newMedia != null) {
            call.respond(HttpStatusCode.Created, "Media uploaded successfully with ID: ${newMedia.id.value}")
        } else {
            call.respond(HttpStatusCode.NotFound, "Post not found or media upload failed") // Post not found is now a possible error
        }
    }

    get("/media/{media_id}") {
        val mediaId = call.parameters["media_id"]?.toULongOrNull()
        if (mediaId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid media ID")
            return@get
        }

        val mediaContent = mediaService.getMediaContent(mediaId)
        if (mediaContent == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        val (mediaRow, channel) = mediaContent
        call.response.header(HttpHeaders.ContentType, mediaRow.mimeType)
        call.respond(channel)
    }
}