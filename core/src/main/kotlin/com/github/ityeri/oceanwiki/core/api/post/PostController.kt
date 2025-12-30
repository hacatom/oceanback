package com.github.ityeri.oceanwiki.core.api.post

import com.github.ityeri.oceanwiki.core.post.dto.CreatePostRequest
import com.github.ityeri.oceanwiki.core.location.Point
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Routing.postController(postService: PostService) {
    // Hardcoded author ID for now, as user authentication is not implemented yet
    val testAuthorId = 1UL // This needs to be consistent with MediaService's testUserId

    post("/posts") {
        val request = call.receive<CreatePostRequest>()
        val coords = Point(request.latitude, request.longitude)

        val newPost = postService.createPost(testAuthorId, coords, request.title, request.content, request.category)

        if (newPost != null) {
            call.respond(HttpStatusCode.Created, newPost)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create post")
        }
    }

    get("/posts/{id}") {
        val postId = call.parameters["id"]?.toULongOrNull()
        if (postId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid post ID format")
            return@get
        }

        val post = postService.getPost(postId)
        if (post != null) {
            call.respond(HttpStatusCode.OK, post)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/posts/{id}/upvote") {
        val postId = call.parameters["id"]?.toULongOrNull()
        if (postId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid post ID format")
            return@post
        }

        val updatedPost = postService.upvotePost(postId)
        if (updatedPost != null) {
            call.respond(HttpStatusCode.OK, updatedPost)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/posts/{id}/downvote") {
        val postId = call.parameters["id"]?.toULongOrNull()
        if (postId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid post ID format")
            return@post
        }

        val updatedPost = postService.downvotePost(postId)
        if (updatedPost != null) {
            call.respond(HttpStatusCode.OK, updatedPost)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    get("/posts/all") {
        call.respond(HttpStatusCode.OK, postService.getAllPostIds())
    }

    get("/posts/search") {
        val searchString = call.request.queryParameters["address"]
        if (searchString == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing 'address' query parameter")
            return@get
        }
        val posts = postService.findPostsByFullAddress(searchString)
        call.respond(HttpStatusCode.OK, posts)
    }

    get("/posts/top") {
        val topPosts = postService.getTopPostsByScore(10)
        call.respond(HttpStatusCode.OK, topPosts)
    }
}