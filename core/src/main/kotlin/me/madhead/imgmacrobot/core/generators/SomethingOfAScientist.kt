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
 * Norman Osborn about his scientific expertise.
 */
@ExperimentalPathApi
class SomethingOfAScientist(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<SomethingOfAScientistParsedInlineQuery>(
    templatesDir,
    "something of a scientist.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex = "scientist:(\\s*)(?<who>.+)".toRegex(RegexOption.IGNORE_CASE)
        private val regex = "(You know, +)?((I'?m)|(I +am)) +something +of(( +a +)|( +))(?<who>.+?)(( +myself)|(\\p{Punct}.*))?"
            .toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): SomethingOfAScientistParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.get("who")?.let { SomethingOfAScientistParsedInlineQuery(it.value) }
            ?: regex.matchEntire(inlineQuery.query)?.groups?.get("who")?.let { SomethingOfAScientistParsedInlineQuery(it.value) }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: SomethingOfAScientistParsedInlineQuery) {
        imageMacroParagraph("YOU KNOW") {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph("I'm something of a ${parsedInlineQuery.who} myself".toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [SomethingOfAScientist].
 *
 * @property who something of what occupation?
 */
data class SomethingOfAScientistParsedInlineQuery(
    val who: String
) : ParsedInlineQuery {
    override val discriminator: String
        get() = who
}
