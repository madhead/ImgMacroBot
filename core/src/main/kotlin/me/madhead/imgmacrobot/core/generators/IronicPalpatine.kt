package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.MeterRegistry
import me.madhead.imgmacrobot.core.CachingImageMacroGenerator
import me.madhead.imgmacrobot.core.EmptyParsedInlineQuery
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.imgur.ImageUploadRequest
import me.madhead.imgmacrobot.imgur.Imgur
import org.apache.logging.log4j.LogManager
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
    registry: MeterRegistry? = null,
) : CachingImageMacroGenerator<EmptyParsedInlineQuery>(registry) {
    companion object {
        private val logger = LogManager.getLogger(IronicPalpatine::class.java)!!
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): EmptyParsedInlineQuery? {
        return if (inlineQuery.query.isNotBlank() &&
            ((inlineQuery.query.contains("ironic", ignoreCase = true)) || ("ironic".contains(inlineQuery.query, ignoreCase = true)))) {
            EmptyParsedInlineQuery
        } else {
            null
        }
    }

    override fun cached(parsedInlineQuery: ParsedInlineQuery): InlineQueryResult? {
        return null
    }

    override fun cache(parsedInlineQuery: ParsedInlineQuery, result: InlineQueryResult) {
        Unit
    }

    override suspend fun generate(parsedInlineQuery: EmptyParsedInlineQuery): InlineQueryResult? {
        logger.info("Generating image macro")

        return templatesDir.resolve("ironic.jpeg").takeIf { it.isRegularFile() }?.let { file ->
            val upload = imgur.imageUpload(ImageUploadRequest(
                image = file.readBytes(),
                name = "ironic.jpeg"
            ))
            val data = upload.data

            logger.debug("Imgur upload result: {}", data)

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
