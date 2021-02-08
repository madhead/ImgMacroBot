package me.madhead.imgmacrobot.imgur

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Image upload response body.
 *
 * @property status status.
 * @property success operation result..
 * @property data response details.
 */
@Serializable
data class ImageUploadResponseBody(
        @SerialName("status")
        val status: Int,

        @SerialName("success")
        val success: Boolean,

        @SerialName("data")
        val data: ImageUploadResponseBodyData,
)
