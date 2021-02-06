package me.madhead.imgmacrobot.imgur

/**
 * Imgur image upload response body and headers.
 *
 * @property body image upload response body.
 * @property headers image upload response headers.
 */
data class ImageUploadResponse(
        val body: ImageUploadResponseBody,
        val headers: ImageUploadResponseHeaders,
)
