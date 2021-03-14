package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.MeterRegistry
import me.madhead.imgmacrobot.core.CacheableInlineQueryResult
import me.madhead.imgmacrobot.core.CachingImageMacroGenerator
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResultType
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
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : CachingImageMacroGenerator<IronicPalpatineParsedInlineQuery>(cachedInlineQueryResultDAO, registry) {
    companion object {
        private val logger = LogManager.getLogger(IronicPalpatine::class.java)!!
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): IronicPalpatineParsedInlineQuery? {
        return if (inlineQuery.query.isNotBlank() &&
            ((inlineQuery.query.contains("ironic", ignoreCase = true)) || ("ironic".contains(inlineQuery.query, ignoreCase = true)))) {
            IronicPalpatineParsedInlineQuery
        } else {
            null
        }
    }

    override suspend fun generateCacheable(parsedInlineQuery: IronicPalpatineParsedInlineQuery): CacheableInlineQueryResult? {
        logger.info("Generating image macro")

        return templatesDir.resolve("ironic.jpeg").takeIf { it.isRegularFile() }?.let { file ->
            val upload = imgur.imageUpload(ImageUploadRequest(
                image = file.readBytes(),
                name = "ironic.jpeg"
            ))
            val data = upload.data

            logger.debug("Imgur upload result: {}", data)

            CacheableInlineQueryResult(
                inlineQueryResult = InlineQueryResultPhotoImpl(
                    id = UUID.randomUUID().toString(),
                    url = data.link,
                    thumbUrl = data.link,
                    width = data.width,
                    height = data.height,
                ),
                type = CachedInlineQueryResultType.PHOTO,
                url = data.link,
                width = data.width,
                height = data.height,
                id = data.id,
                deleteHash = data.deleteHash,
            )
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [IronicPalpatine].
 */
object IronicPalpatineParsedInlineQuery : ParsedInlineQuery {
    override val discriminator: String
        get() = "_"
}
