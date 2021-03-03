package me.madhead.imgmacrobot.imgur

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Imgur's image upload response.
 *
 * @property status status.
 * @property success operation result..
 * @property data response details.
 */
@Serializable
data class ImageUploadResponse(
    @SerialName("status")
    val status: Int,

    @SerialName("success")
    val success: Boolean,

    @SerialName("data")
    val data: ImageUploadResponseData,
)
