package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.ParsingImageMacroGenerator
import me.madhead.imgmacrobot.imgur.ImageUploadRequest
import me.madhead.imgmacrobot.imgur.ImageUploadResponseBodyDataSuccess
import me.madhead.imgmacrobot.imgur.Imgur
import org.apache.logging.log4j.LogManager
import org.jetbrains.skija.Data
import org.jetbrains.skija.EncodedImageFormat
import org.jetbrains.skija.FontMgr
import org.jetbrains.skija.FontStyle
import org.jetbrains.skija.Image
import org.jetbrains.skija.Surface
import org.jetbrains.skija.Typeface
import org.jetbrains.skija.paragraph.Alignment
import org.jetbrains.skija.paragraph.FontCollection
import org.jetbrains.skija.paragraph.HeightMode
import org.jetbrains.skija.paragraph.ParagraphBuilder
import org.jetbrains.skija.paragraph.ParagraphStyle
import org.jetbrains.skija.paragraph.Shadow
import org.jetbrains.skija.paragraph.StrutStyle
import org.jetbrains.skija.paragraph.TextStyle
import org.jetbrains.skija.paragraph.TypefaceFontProvider
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes
import kotlin.math.abs

/**
 * Morpheus from the "The Matrix" trying to tell you something.
 */
@ExperimentalPathApi
@Suppress("LongMethod", "MagicNumber", "ForbiddenComment", "ComplexMethod", "ReturnCount")
class WhatIfIToldYou(
        private val templatesDir: Path,
        private val fontsDir: Path,
        private val imgur: Imgur,
) : ParsingImageMacroGenerator<WhatIfIToldYouParsedInlineQuery> {
    companion object {
        private val logger = LogManager.getLogger(WhatIfIToldYou::class.java)!!
        private val regex = "What +if +I +told +you ++(.+)".toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): WhatIfIToldYouParsedInlineQuery? {
        return regex
                .matchEntire(inlineQuery.query)
                ?.destructured
                ?.let { (whatIfIToldMeWhat) ->
                    WhatIfIToldYouParsedInlineQuery(whatIfIToldMeWhat)
                }
    }

    override suspend fun generate(parsedInlineQuery: WhatIfIToldYouParsedInlineQuery): InlineQueryResult? {
        logger.info("Generating image macro")

        // TODO: Caching fonts
        val templatePath = templatesDir.resolve("WhatIfIToldYou.png").takeIf { it.isRegularFile() } ?: return null
        val fontPath = fontsDir.resolve("Anton.ttf").takeIf { it.isRegularFile() } ?: return null
        val template = Image.makeFromEncoded(templatePath.readBytes()) ?: return null
        val fontData = Data.makeFromBytes(fontPath.readBytes()) ?: return null
        val font = Typeface.makeFromData(fontData)
        val fontProvider = TypefaceFontProvider()
        val fontCollection = FontCollection()

        fontProvider.registerTypeface(font)
        fontCollection.setDefaultFontManager(FontMgr.getDefault())
        fontCollection.setAssetFontManager(fontProvider)

        val surface = Surface.makeRasterN32Premul(template.width, template.height)
        val canvas = surface.canvas

        canvas.drawImage(template, 0F, 0F)

        ParagraphStyle().use { paragraphStyle ->
            paragraphStyle.alignment = Alignment.CENTER
            ParagraphBuilder(paragraphStyle, fontCollection).use { paragraphBuilder ->
                val textStyle = TextStyle().apply {
                    color = 0xFFFFFFFF.toInt()
                    fontSize = 48F
                    setFontFamily("Anton")
                    addShadows(arrayOf(
                            Shadow(0xFF000000.toInt(), 1F, 0F, 2.toDouble()),
                            Shadow(0xFF000000.toInt(), 0F, 1F, 2.toDouble()),
                            Shadow(0xFF000000.toInt(), -1F, 0F, 2.toDouble()),
                            Shadow(0xFF000000.toInt(), 0F, -1F, 2.toDouble()),
                    ))
                }

                paragraphBuilder.pushStyle(textStyle)
                paragraphBuilder.addText("WHAT IF I TOLD YOU")

                val paragraph = paragraphBuilder.build() ?: return null

                paragraph.layout(template.width.toFloat())
                paragraph.paint(canvas, 0F, 10F)
            }
        }

        ParagraphStyle().use { paragraphStyle ->
            paragraphStyle.alignment = Alignment.CENTER
            ParagraphBuilder(paragraphStyle, fontCollection).use { paragraphBuilder ->
                val textStyle = TextStyle().apply {
                    color = 0xFFFFFFFF.toInt()
                    fontSize = 48F
                    setFontFamily("Anton")
                    addShadows(arrayOf(
                            Shadow(0xFF000000.toInt(), 1F, 0F, 2.toDouble()),
                            Shadow(0xFF000000.toInt(), 0F, 1F, 2.toDouble()),
                            Shadow(0xFF000000.toInt(), -1F, 0F, 2.toDouble()),
                            Shadow(0xFF000000.toInt(), 0F, -1F, 2.toDouble()),
                    ))
                }

                paragraphBuilder.pushStyle(textStyle)
                paragraphBuilder.addText(parsedInlineQuery.whatIfYouToldMeWhat.toUpperCase())

                val paragraph = paragraphBuilder.build() ?: return null

                paragraph.layout(0.9F * template.width.toFloat())
                paragraph.paint(canvas, 0.05F * template.width, template.height - 10 - paragraph.height)
            }
        }

        val snapshot = surface.makeImageSnapshot() ?: return null
        val data = snapshot.encodeToData(EncodedImageFormat.JPEG, 100) ?: return null
        val upload = imgur.imageUpload(ImageUploadRequest(
                image = data.bytes,
                name = "what if i told you.jpeg"
        ))
        val responseData = upload.body.data

        logger.debug("Imgur upload result: {}", responseData)

        return if (responseData !is ImageUploadResponseBodyDataSuccess) {
            null
        } else {
            InlineQueryResultPhotoImpl(
                    id = UUID.randomUUID().toString(),
                    url = responseData.link,
                    thumbUrl = responseData.link,
                    width = responseData.width,
                    height = responseData.height,
            )
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [WhatIfIToldYou].
 *
 * @property whatIfYouToldMeWhat so what is Morpheus trying to told us?
 */
data class WhatIfIToldYouParsedInlineQuery(
        val whatIfYouToldMeWhat: String
) : ParsedInlineQuery