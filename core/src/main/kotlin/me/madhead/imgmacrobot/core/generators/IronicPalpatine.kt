package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import me.madhead.imgmacrobot.core.ImageMacroGenerator
import me.madhead.imgmacrobot.imgur.ImageUploadRequest
import me.madhead.imgmacrobot.imgur.ImageUploadResponseBodyDataSuccess
import me.madhead.imgmacrobot.imgur.Imgur
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.isRegularFile
import kotlin.io.path.readBytes

/**
 * Palpatine's reaction to the tragedy of Darth Plagueis the Wise.
 */
@ExperimentalPathApi
class IronicPalpatine(
        private val templatesDir: Path,
        private val imgur: Imgur,
) : ImageMacroGenerator {
    override suspend fun generate(inlineQuery: InlineQuery): InlineQueryResult? {
        return templatesDir.resolve("ironic.jpeg").takeIf { it.isRegularFile() }?.let { file ->
            val upload = imgur.imageUpload(ImageUploadRequest(
                    image = file.readBytes(),
                    name = "ironic.jpeg"
            ))
            val data = upload.body.data

            if (data !is ImageUploadResponseBodyDataSuccess) {
                null
            } else {
                InlineQueryResultPhotoImpl(
                        id = UUID.randomUUID().toString(),
                        url = data.link,
                        thumbUrl = data.link,
                        width = data.width,
                        height = data.height,
                )
            }
        }
    }
}
