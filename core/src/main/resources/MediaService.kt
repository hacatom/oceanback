import io.ktor.utils.io.readAvailable
import kotlin.io.use

class MediaService(val rootPath: java.nio.file.Path) {
    suspend fun upload(byteChannel: io.ktor.utils.io.ByteReadChannel, mediaType: com.github.ityeri.oceanwiki.core.media.orm.MediaType) {
        val randomBytes = ByteArray(48) // base64로 64글자 정도
        java.security.SecureRandom().nextBytes(randomBytes)
        val fileName = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
        val filePath = rootPath.resolve("$fileName.bin")

        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            java.nio.file.Files.newByteChannel(
                filePath,
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.WRITE,
                java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
            )
        }.use { out ->
            val buffer = ByteArray(8192)
            while (!byteChannel.isClosedForRead) {
                val bytesRead = byteChannel.readAvailable(buffer)
                if (bytesRead > 0) {
                    out.write(java.nio.ByteBuffer.wrap(buffer, 0, bytesRead))
                }
            }
        }
    }

//    fun getMedia(mediaId: ULong): ByteReadChannel {
//    }
}