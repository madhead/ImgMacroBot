package me.madhead.imgmacrobot.imgur

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Successful response.
 *
 * @property id resulting image id.
 * @property deleteHash hash required to delete the image.
 * @property title image title.
 * @property description image description.
 * @property name image name.
 * @property type image content type.
 * @property width image width.
 * @property height image height.
 * @property size image size.
 * @property views number of views.
 * @property bandwidth unknown field.
 * @property animated whether the image is animated or not.
 * @property favorite whether the image is favorite or not.
 * @property inGallery whether the image is in gallery or not.
 * @property inMostViral whether the image is most viral or not.
 * @property hasSound whether has sound or not.
 * @property isAd whether the image is an ad.
 * @property link image link.
 * @property datetime unknown field.
 * @property mp4 unknown field.
 * @property hls unknown field.
 */
@Serializable
data class ImageUploadResponseData(
        @SerialName("id")
        val id: String,

        @SerialName("deletehash")
        val deleteHash: String,

        // @SerialName("account_id")
        // val accountId:

        // @SerialName("account_url")
        // val accountUrl:

        // @SerialName("ad_type")
        // val adType:

        // @SerialName("ad_url")
        // val adUrl:

        @SerialName("title")
        val title: String?,

        @SerialName("description")
        val description: String?,

        @SerialName("name")
        val name: String,

        @SerialName("type")
        val type: String,

        @SerialName("width")
        val width: Int,

        @SerialName("height")
        val height: Int,

        @SerialName("size")
        val size: Int,

        @SerialName("views")
        val views: Int,

        // @SerialName("section")
        // val section:

        // @SerialName("vote")
        // val vote:

        @SerialName("bandwidth")
        val bandwidth: Int,

        @SerialName("animated")
        val animated: Boolean,

        @SerialName("favorite")
        val favorite: Boolean,

        @SerialName("in_gallery")
        val inGallery: Boolean,

        @SerialName("in_most_viral")
        val inMostViral: Boolean,

        @SerialName("has_sound")
        val hasSound: Boolean,

        @SerialName("is_ad")
        val isAd: Boolean,

        // @SerialName("nsfw")
        // val nsfw:

        @SerialName("link")
        val link: String,

        // @SerialName("tags")
        // val tags:

        @SerialName("datetime")
        val datetime: Long,

        @SerialName("mp4")
        val mp4: String,

        @SerialName("hls")
        val hls: String,
)
