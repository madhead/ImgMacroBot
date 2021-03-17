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
 * Boromir knows the ways.
 */
@ExperimentalPathApi
class OneDoesNotSimply(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<OneDoesNotSimplyParsedInlineQuery>(
    templatesDir,
    "one does not simply walk into mordor.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex = "boromir:(\\s*)(?<doWhat>.+)".toRegex(RegexOption.IGNORE_CASE)
        private val regex = "One +((does +not)|(doesn't)) +simply ++(?<doWhat>.+)".toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): OneDoesNotSimplyParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.get("doWhat")?.let { OneDoesNotSimplyParsedInlineQuery(it.value) }
            ?: regex.matchEntire(inlineQuery.query)?.groups?.get("doWhat")?.let { OneDoesNotSimplyParsedInlineQuery(it.value) }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: OneDoesNotSimplyParsedInlineQuery) {
        imageMacroParagraph(text = "ONE DOES NOT SIMPLY", size = 32F) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph(text = parsedInlineQuery.doWhat.toUpperCase(), size = 32F) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [OneDoesNotSimply].
 *
 * @property doWhat one does not simply do what?
 */
data class OneDoesNotSimplyParsedInlineQuery(
    val doWhat: String
) : ParsedInlineQuery {
    override val discriminator: String
        get() = doWhat
}
