package com.github.ityeri.oceanwiki.core.api.user

import com.github.ityeri.oceanwiki.core.user.dto.UserResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.userController(userService: UserService) {
    get("/users/{id}") {
        val id = call.parameters["id"]?.toULongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid user ID format")
            return@get
        }

        val user = userService.findUserById(id)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        val userResponse = UserResponse(id = user.id.value, username = user.username)
        call.respond(userResponse)
    }
}
