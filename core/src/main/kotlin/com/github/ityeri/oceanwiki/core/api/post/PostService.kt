package com.github.ityeri.oceanwiki.core.api.post

import com.github.ityeri.oceanwiki.core.location.LocationResolver
import com.github.ityeri.oceanwiki.core.location.Point
import com.github.ityeri.oceanwiki.core.location.impl.orm.LocationRow
import com.github.ityeri.oceanwiki.core.location.impl.orm.LocationTable
import com.github.ityeri.oceanwiki.core.post.dto.PostResponse
import com.github.ityeri.oceanwiki.core.post.dto.toResponse
import com.github.ityeri.oceanwiki.core.user.orm.UserRow
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*

class PostService(private val locationResolver: LocationResolver) {

    suspend fun createPost(authorId: ULong, coords: Point, title: String, content: String, category: Int): PostResponse? {
        val resolvedLocation = locationResolver.resolveLocationFromCoords(coords) ?: return null

        return transaction {
            // Find the author
            val author = UserRow.findById(authorId) ?: return@transaction null

            // Create and save the new LocationRow based on the resolved location info
            val locationRow = LocationRow.new {
                this.latitude = resolvedLocation.point.latitude
                this.longitude = resolvedLocation.point.longitude
                this.fullAddress = resolvedLocation.fullAddress
                this.province = resolvedLocation.province
                this.city = resolvedLocation.city
            }

            // Create and save the new PostRow, linking it to the new LocationRow
            val newPostRow = PostRow.new {
                this.author = author
                this.location = locationRow
                this.title = title
                this.content = content
                this.category = category
            }
            newPostRow.toResponse() // Map to DTO inside the transaction
        }
    }

    fun upvotePost(postId: ULong): PostResponse? {
        return transaction {
            val postRow = PostRow.findById(postId)?.apply {
                this.upvoteCount += 1
            }
            postRow?.toResponse() // Map to DTO inside the transaction
        }
    }

    fun downvotePost(postId: ULong): PostResponse? {
        return transaction {
            val postRow = PostRow.findById(postId)?.apply {
                this.downvoteCount += 1
            }
            postRow?.toResponse() // Map to DTO inside the transaction
        }
    }

    fun getPost(postId: ULong): PostResponse? {
        return transaction {
            PostRow.findById(postId)?.toResponse() // Map to DTO inside the transaction
        }
    }

    fun getAllPostIds(): List<PostResponse> {
        return transaction {
            PostRow.all().map { it.toResponse() }
        }
    }

    fun findPostsByFullAddress(searchString: String): List<PostResponse> {
        return transaction {
            (PostTable innerJoin LocationTable)
                .slice(PostTable.columns) // Select all columns from PostTable
                .select { LocationTable.fullAddress.like("%${searchString}%") }
                .map { PostRow.wrapRow(it).toResponse() } // Map to PostRow and then to PostResponse
        }
    }

    fun getTopPostsByScore(limit: Int): List<PostResponse> {
        return transaction {
            PostTable.selectAll()
                .orderBy(
                    Expression.build { PostTable.upvoteCount - PostTable.downvoteCount },
                    SortOrder.DESC
                ).limit(limit)
                .map { PostRow.wrapRow(it).toResponse() }
        }
    }
}
