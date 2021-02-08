package me.madhead.imgmacrobot.imgur.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import me.madhead.imgmacrobot.imgur.ImageUploadRequest
import me.madhead.imgmacrobot.imgur.ImageUploadResponse
import me.madhead.imgmacrobot.imgur.ImageUploadResponseHeaders
import me.madhead.imgmacrobot.imgur.Imgur
import org.apache.logging.log4j.LogManager

/**
 * [Ktor-based](https://ktor.io) [Imgur] implementation.
 */
class KtorImgur : Imgur {
    companion object {
        private val logger = LogManager.getLogger(KtorImgur::class.java)!!
    }

    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    KtorImgur.logger.debug(message)
                }
            }
            level = LogLevel.ALL
        }
    }

    override suspend fun imageUpload(request: ImageUploadRequest): ImageUploadResponse {
        val response = client.submitFormWithBinaryData<HttpResponse>(
                formData {
                    append(
                            key = "image",
                            value = request.image,
                            headers = Headers.build {
                                request.name?.let {
                                    this[HttpHeaders.ContentDisposition] = "filename=${request.name}"
                                }
                            }
                    )
                    request.title?.let {
                        append("title", it)
                    }
                    request.description?.let {
                        append("description", it)
                    }
                }
        ) {
            url("https://api.imgur.com/3/upload")
        }

        return ImageUploadResponse(
                body = response.receive(),
                headers = ImageUploadResponseHeaders(
                        postRateLimitLimit = response.headers["X-Post-Rate-Limit-Limit"]?.toIntOrNull(),
                        postRateLimitRemaining = response.headers["X-Post-Rate-Limit-Remaining"]?.toIntOrNull(),
                        postRateLimitReset = response.headers["X-Post-Rate-Limit-Reset"]?.toIntOrNull(),
                        rateLimitClientLimit = response.headers["X-Ratelimit-Clientlimit"]?.toIntOrNull(),
                        rateLimitClientRemaining = response.headers["X-Ratelimit-Clientremaining"]?.toIntOrNull(),
                        rateLimitClientReset = response.headers["X-Ratelimit-Clientreset"]?.toIntOrNull(),
                        rateLimitUserLimit = response.headers["X-Ratelimit-Userlimit"]?.toIntOrNull(),
                        rateLimitUserRemaining = response.headers["X-Ratelimit-Userremaining"]?.toIntOrNull(),
                        rateLimitUserReset = response.headers["X-Ratelimit-Userreset"]?.toLongOrNull(),
                )
        )
    }

    override fun close() {
        client.close()
    }
}
