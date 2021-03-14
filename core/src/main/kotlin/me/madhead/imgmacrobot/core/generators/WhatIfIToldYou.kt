package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.MeterRegistry
import me.madhead.imgmacrobot.core.ParagraphsImageMacroGenerator
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.imgur.Imgur
import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Image
import org.jetbrains.skija.paragraph.FontCollection
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi

/**
 * Morpheus from the "The Matrix" trying to tell you something.
 */
@ExperimentalPathApi
class WhatIfIToldYou(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<WhatIfIToldYouParsedInlineQuery>(
    templatesDir,
    "what if i told you.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
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

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: WhatIfIToldYouParsedInlineQuery) {
        imageMacroParagraph("WHAT IF I TOLD YOU") {
            layout(template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0F, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph(parsedInlineQuery.whatIfYouToldMeWhat.toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
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
) : ParsedInlineQuery {
    override val discriminator: String
        get() = whatIfYouToldMeWhat
}
