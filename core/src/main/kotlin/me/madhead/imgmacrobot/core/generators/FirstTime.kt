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
 * Never try your luck twice.
 */
@ExperimentalPathApi
class FirstTime(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<FirstTimeParsedInlineQuery>(
    templatesDir,
    "first time.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex =
            "(first time):(\\s*)(?<bottom>.+?)"
                .toRegex(RegexOption.IGNORE_CASE)
    }

    @Suppress("ReturnCount")
    override fun parseInlineQuery(inlineQuery: InlineQuery): FirstTimeParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.let { groups ->
            FirstTimeParsedInlineQuery(
                groups["bottom"]?.value ?: return null,
            )
        }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: FirstTimeParsedInlineQuery) {
        imageMacroParagraph(parsedInlineQuery.bottom.toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [FirstTime].
 *
 * @property bottom bottom text.
 */
data class FirstTimeParsedInlineQuery(
    val bottom: String,
) : ParsedInlineQuery {
    override val discriminator: String
        get() = bottom
}
