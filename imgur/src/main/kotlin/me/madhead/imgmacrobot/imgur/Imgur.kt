package me.madhead.imgmacrobot.imgur

import java.io.Closeable

/**
 * [Imgur API](https://apidocs.imgur.com) is used to upload and store image macros.
 */
interface Imgur : Closeable {
    /**
     * Upload a new image.
     */
    suspend fun imageUpload(request: ImageUploadRequest): ImageUploadResponse
}
