package com.github.ityeri.oceanwiki.core.post

import com.github.ityeri.oceanwiki.core.location.impl.orm.LocationRow
import com.github.ityeri.oceanwiki.core.location.impl.orm.LocationTable
import com.github.ityeri.oceanwiki.core.media.orm.MediaRow
import com.github.ityeri.oceanwiki.core.media.orm.MediaTable
import com.github.ityeri.oceanwiki.core.user.orm.UserRow
import com.github.ityeri.oceanwiki.core.user.orm.UserTable
import org.jetbrains.exposed.dao.ULongEntity
import org.jetbrains.exposed.dao.ULongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.ULongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PostTable : ULongIdTable("posts") {
    val author = reference("author", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val location = reference("location", LocationTable.id, onDelete = ReferenceOption.CASCADE)
    val category = integer("category") // 0: 보존구역, 1: 위험
    val title = varchar("title", 255)
    val content = text("content")
    val upvoteCount = integer("upvote_count").default(0)
    val downvoteCount = integer("downvote_count").default(0)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

class PostRow(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<PostRow>(PostTable)

    var author by UserRow referencedOn PostTable.author
    var location by LocationRow referencedOn PostTable.location
    var category by PostTable.category
    var title by PostTable.title
    var content by PostTable.content
    var upvoteCount by PostTable.upvoteCount
    var downvoteCount by PostTable.downvoteCount
    var createdAt by PostTable.createdAt
    var updatedAt by PostTable.updatedAt

    val medias by MediaRow referrersOn MediaTable.post // New: One-to-many relationship with Media
}
