package me.madhead.imgmacrobot.imgur.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.ResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.micrometer.core.instrument.MultiGauge
import io.micrometer.core.instrument.Tags
import kotlinx.serialization.json.Json
import me.madhead.imgmacrobot.imgur.ImageUploadRequest
import me.madhead.imgmacrobot.imgur.ImageUploadResponse
import me.madhead.imgmacrobot.imgur.Imgur
import org.apache.logging.log4j.LogManager

/**
 * [Ktor-based](https://ktor.io) [Imgur] implementation.
 */
class KtorImgur(
        private val clientId: String? = null,
        private val limits: MultiGauge? = null,
) : Imgur {
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
        expectSuccess = false
    }

    override suspend fun imageUpload(request: ImageUploadRequest): ImageUploadResponse {
        val response = upload(request).also { updateMetrics(it) }

        return if (response.status.isSuccess()) {
            response.receive()
        } else {
            throw ResponseException(response, response.status.description)
        }
    }

    override fun close() {
        client.close()
    }

    private suspend fun upload(request: ImageUploadRequest) =
            client.submitFormWithBinaryData<HttpResponse>(
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
                clientId?.let {
                    header("Authorization", "Client-ID ${it}")
                }
            }

    private fun updateMetrics(response: HttpResponse) =
            limits?.let {
                val limitsHeaders = listOf(
                        "X-Post-Rate-Limit-Limit",
                        "X-Post-Rate-Limit-Remaining",
                        "X-RateLimit-ClientLimit",
                        "X-RateLimit-ClientRemaining",
                        "X-RateLimit-UserLimit",
                        "X-RateLimit-UserRemaining",
                        "X-Post-Rate-Limit-Reset",
                        "X-Ratelimit-Userreset",
                )

                it.register(
                        limitsHeaders
                                .map { header ->
                                    MultiGauge.Row.of(
                                            Tags.of("limit", header),
                                            response.headers[header]?.toDoubleOrNull() ?: 0
                                    )
                                },
                        true
                )
            }
}
