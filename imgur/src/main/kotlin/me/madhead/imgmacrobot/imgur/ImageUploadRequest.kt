package me.madhead.imgmacrobot.imgur

/**
 * Imgur image upload request.
 *
 * @property image the image to upload.
 * @property name the name of the file.
 * @property title the title of the image.
 * @property description the description of the image.
 */
data class ImageUploadRequest(
    val image: ByteArray,
    val name: String? = null,
    val title: String? = null,
    val description: String? = null,
)
