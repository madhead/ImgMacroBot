package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import io.micrometer.core.instrument.MeterRegistry
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
 * An [ImageMacroGenerator] who returns a file without any modifications (drawing).
 */
abstract class StaticImageMacroGenerator<T : ParsedInlineQuery>(
    private val templatesDir: Path,
    private val template: String,
    private val imgur: Imgur,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry
) : CachingImageMacroGenerator<T>(cachedInlineQueryResultDAO, registry) {
    companion object {
        private val logger = LogManager.getLogger(StaticImageMacroGenerator::class.java)!!
    }

    @ExperimentalPathApi
    override suspend fun generateCacheable(parsedInlineQuery: T): CacheableInlineQueryResult? {
        logger.info("Generating image macro")

        return templatesDir.resolve(template).takeIf { it.isRegularFile() }?.let { file ->
            val upload = imgur.imageUpload(ImageUploadRequest(
                image = file.readBytes(),
                name = template
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
