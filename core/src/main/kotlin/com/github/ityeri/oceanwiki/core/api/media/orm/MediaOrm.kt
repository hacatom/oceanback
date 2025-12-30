package com.github.ityeri.oceanwiki.core.api.media.orm

import com.github.ityeri.oceanwiki.core.post.PostRow
import com.github.ityeri.oceanwiki.core.post.PostTable
import com.github.ityeri.oceanwiki.core.user.orm.UserRow
import com.github.ityeri.oceanwiki.core.user.orm.UserTable
import org.jetbrains.exposed.dao.ULongEntity
import org.jetbrains.exposed.dao.ULongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.ULongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

enum class MediaType {
    IMAGE,
    VIDEO
}

object MediaTable : ULongIdTable("medias") {
    val author = reference("author", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val post = reference("post", PostTable.id, onDelete = ReferenceOption.CASCADE) // Changed to non-nullable
    val mimeType = varchar("mime_type", length = 32)
    val path = varchar("path", length = 255)
}

class MediaRow(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<MediaRow>(MediaTable)

    var author by UserRow referencedOn MediaTable.author
    var post by PostRow referencedOn MediaTable.post // Changed to non-nullable reference
    var mimeType by MediaTable.mimeType
    var path by MediaTable.path
}
