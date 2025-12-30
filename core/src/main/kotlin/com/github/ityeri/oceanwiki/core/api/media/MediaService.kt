package com.github.ityeri.oceanwiki.core.api.media

import com.github.ityeri.oceanwiki.core.config.AppConfig
import com.github.ityeri.oceanwiki.core.media.orm.MediaRow
import com.github.ityeri.oceanwiki.core.post.PostRow
import com.github.ityeri.oceanwiki.core.user.orm.UserRow
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.SecureRandom
import java.util.Base64

class MediaService(private val config: AppConfig) {
    private val rootStorePath = config.rootStorePath
    private val testUserId = 1UL // Keep the hardcoded user for now

    suspend fun saveMedia(channel: ByteReadChannel, contentType: String, postId: ULong): MediaRow? {
        val fileName = Base64.getUrlEncoder().withoutPadding().encodeToString(SecureRandom().generateSeed(48))
        val filePath = rootStorePath.resolve("$fileName.bin")

        withContext(Dispatchers.IO) {
            Files.newOutputStream(filePath).use { out ->
                channel.copyTo(out)
            }
        }

        return transaction {
            val userRow = UserRow.findById(testUserId) ?: return@transaction null
            val postRow = PostRow.findById(postId) ?: return@transaction null // Validate post existence

            MediaRow.new {
                author = userRow
                post = postRow // Associate with the post
                mimeType = contentType
                path = filePath.toString()
            }
        }
    }

    suspend fun getMediaContent(mediaId: ULong): Pair<MediaRow, ByteReadChannel>? {
        val mediaRow = transaction {
            MediaRow.findById(mediaId)
        } ?: return null

        val filePath = Paths.get(mediaRow.path)
        if (!Files.exists(filePath)) {
            return null
        }

        return withContext(Dispatchers.IO) {
            val inputStream: InputStream = Files.newInputStream(filePath)
            mediaRow to inputStream.toByteReadChannel()
        }
    }
}
