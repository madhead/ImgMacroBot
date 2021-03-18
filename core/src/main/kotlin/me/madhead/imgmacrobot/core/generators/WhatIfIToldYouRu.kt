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
 * Morpheus from the "The Matrix" trying to tell you something (russian).
 */
@ExperimentalPathApi
class WhatIfIToldYouRu(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<WhatIfIToldYouRuParsedInlineQuery>(
    templatesDir,
    "what if i told you.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex = "морфеус:(\\s*)(?<what>.+)".toRegex(RegexOption.IGNORE_CASE)
        private val regex = "Что +если +я +скажу +тебе,? ++(?<what>.+)".toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): WhatIfIToldYouRuParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.get("what")?.let { WhatIfIToldYouRuParsedInlineQuery(it.value) }
            ?: regex.matchEntire(inlineQuery.query)?.groups?.get("what")?.let { WhatIfIToldYouRuParsedInlineQuery(it.value) }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: WhatIfIToldYouRuParsedInlineQuery) {
        imageMacroParagraph("ЧТО ЕСЛИ Я СКАЖУ ТЕБЕ") {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph(parsedInlineQuery.whatIfYouToldMeWhat.toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [WhatIfIToldYouRu].
 *
 * @property whatIfYouToldMeWhat so what is Morpheus trying to told us?
 */
data class WhatIfIToldYouRuParsedInlineQuery(
    val whatIfYouToldMeWhat: String
) : ParsedInlineQuery {
    override val discriminator: String
        get() = whatIfYouToldMeWhat
}
