package com.github.ityeri.oceanwiki.core.api.user.orm

import org.jetbrains.exposed.dao.ULongEntity
import org.jetbrains.exposed.dao.ULongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.ULongIdTable

object UserTable : ULongIdTable("users") {
    val username = varchar("username", length = 255)
    val password = binary("password", length = 64)
}

class UserRow(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<UserRow>(UserTable)

    val username by UserTable.username
    val password by UserTable.password
}
