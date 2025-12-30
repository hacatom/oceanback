package com.github.ityeri.oceanwiki.core.api.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: ULong,
    val username: String
)
