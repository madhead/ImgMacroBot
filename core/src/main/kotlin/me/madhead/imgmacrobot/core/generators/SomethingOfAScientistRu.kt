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
class SomethingOfAScientistRu(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<SomethingOfAScientistRuParsedInlineQuery>(
    templatesDir,
    "something of a scientist.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex = "уч[её]ный:(\\s*)(?<who>.+)".toRegex(RegexOption.IGNORE_CASE)
        private val regex = "(Знаете, +)?Я(( +и +)|( +))+сам +своего +рода +(?<who>.+?)".toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): SomethingOfAScientistRuParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.get("who")?.let { SomethingOfAScientistRuParsedInlineQuery(it.value) }
            ?: regex.matchEntire(inlineQuery.query)?.groups?.get("who")?.let { SomethingOfAScientistRuParsedInlineQuery(it.value) }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: SomethingOfAScientistRuParsedInlineQuery) {
        imageMacroParagraph("ЗНАЕТЕ") {
            layout(template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0F, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph("Я И САМ СВОЕГО РОДА ${parsedInlineQuery.who}".toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [SomethingOfAScientistRu].
 *
 * @property who something of what occupation?
 */
data class SomethingOfAScientistRuParsedInlineQuery(
    val who: String
) : ParsedInlineQuery {
    override val discriminator: String
        get() = who
}
