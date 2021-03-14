package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import io.micrometer.core.instrument.MeterRegistry
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResultType
import me.madhead.imgmacrobot.imgur.ImageUploadRequest
import me.madhead.imgmacrobot.imgur.Imgur
import org.apache.logging.log4j.LogManager
import org.jetbrains.skija.Canvas
import org.jetbrains.skija.EncodedImageFormat
import org.jetbrains.skija.FontSlant
import org.jetbrains.skija.FontStyle
import org.jetbrains.skija.FontWeight
import org.jetbrains.skija.FontWidth
import org.jetbrains.skija.Image
import org.jetbrains.skija.Surface
import org.jetbrains.skija.paragraph.Alignment
import org.jetbrains.skija.paragraph.FontCollection
import org.jetbrains.skija.paragraph.Paragraph
import org.jetbrains.skija.paragraph.ParagraphBuilder
import org.jetbrains.skija.paragraph.ParagraphStyle
import org.jetbrains.skija.paragraph.Shadow
import org.jetbrains.skija.paragraph.TextStyle
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.isRegularFile
import kotlin.io.path.readBytes

/**
 * An [ImageMacroGenerator] who draws text paragraphs on a template image.
 */
@ExperimentalPathApi
abstract class ParagraphsImageMacroGenerator<T : ParsedInlineQuery>(
    private val templatesDir: Path,
    private val template: String,
    private val imgur: Imgur,
    private val fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry
) : CachingImageMacroGenerator<T>(cachedInlineQueryResultDAO, registry) {
    companion object {
        private val logger = LogManager.getLogger(ParagraphsImageMacroGenerator::class.java)!!
    }

    @Suppress("ReturnCount")
    override suspend fun generateCacheable(parsedInlineQuery: T): CacheableInlineQueryResult? {
        logger.info("Generating image macro")

        val templatePath = templatesDir.resolve(template).takeIf { it.isRegularFile() } ?: return null
        val templateImage = Image.makeFromEncoded(templatePath.readBytes()) ?: return null
        val surface = Surface.makeRasterN32Premul(templateImage.width, templateImage.height)
        val canvas = surface.canvas

        canvas.drawImage(templateImage, @Suppress("MagicNumber") 0F, @Suppress("MagicNumber") 0F)
        drawParagraphs(templateImage, canvas, parsedInlineQuery)

        val snapshot = surface.makeImageSnapshot() ?: return null
        val data = snapshot.encodeToData(EncodedImageFormat.JPEG, @Suppress("MagicNumber") 95) ?: return null

        val upload = imgur.imageUpload(ImageUploadRequest(
            image = data.bytes,
            name = template
        ))
        val responseData = upload.data

        logger.debug("Imgur upload result: {}", responseData)

        return CacheableInlineQueryResult(
            inlineQueryResult = InlineQueryResultPhotoImpl(
                id = UUID.randomUUID().toString(),
                url = responseData.link,
                thumbUrl = responseData.link,
                width = responseData.width,
                height = responseData.height,
            ),
            type = CachedInlineQueryResultType.PHOTO,
            url = responseData.link,
            width = responseData.width,
            height = responseData.height,
            id = responseData.id,
            deleteHash = responseData.deleteHash,
        )
    }

    /**
     * Implement this method to draw the text you want in your image macro.
     */
    abstract fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: T)

    @Suppress("LongParameterList")
    protected fun imageMacroParagraph(
        text: String,
        textColor: Int = @Suppress("MagicNumber") 0xFFFFFFFF.toInt(),
        size: Float = @Suppress("MagicNumber") 48F,
        shadow: Double = @Suppress("MagicNumber") 2.0,
        shadowColor: Int = @Suppress("MagicNumber") 0xFF000000.toInt(),
        action: Paragraph.() -> Unit
    ) {
        ParagraphStyle().use { paragraphStyle ->
            paragraphStyle.alignment = Alignment.CENTER
            ParagraphBuilder(paragraphStyle, fontCollection).use { paragraphBuilder ->
                val textStyle = TextStyle().apply {
                    color = textColor
                    fontSize = size
                    fontStyle = FontStyle(FontWeight.BLACK, FontWidth.CONDENSED, FontSlant.UPRIGHT)
                    setFontFamily("Oswald")
                    addShadows(arrayOf(
                        Shadow(shadowColor, 1F, 0F, shadow),
                        Shadow(shadowColor, 0F, 1F, shadow),
                        Shadow(shadowColor, -1F, 0F, shadow),
                        Shadow(shadowColor, 0F, -1F, shadow),
                    ))
                }

                paragraphBuilder.pushStyle(textStyle)
                paragraphBuilder.addText(text)

                val paragraph = paragraphBuilder.build() ?: return

                paragraph.action()
            }
        }
    }
}
