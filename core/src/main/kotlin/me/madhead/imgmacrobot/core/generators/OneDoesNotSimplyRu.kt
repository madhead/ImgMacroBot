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
 * Boromir knows the ways (russian).
 */
@ExperimentalPathApi
class OneDoesNotSimplyRu(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<OneDoesNotSimplyRuParsedInlineQuery>(
    templatesDir,
    "one does not simply walk into mordor.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex = "боромир:(\\s*)(?<doWhat>.+)".toRegex(RegexOption.IGNORE_CASE)
        private val regex = "Нельзя +просто +так( +взять и)? ++(?<doWhat>.+)".toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): OneDoesNotSimplyRuParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.get("doWhat")?.let { OneDoesNotSimplyRuParsedInlineQuery(it.value) }
            ?: regex.matchEntire(inlineQuery.query)?.groups?.get("doWhat")?.let { OneDoesNotSimplyRuParsedInlineQuery(it.value) }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: OneDoesNotSimplyRuParsedInlineQuery) {
        imageMacroParagraph(text = "НЕЛЬЗЯ ПРОСТО ТАК ВЗЯТЬ И", size = 32F) {
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
 * [ParsedInlineQuery] implementation for [OneDoesNotSimplyRu].
 *
 * @property doWhat one does not simply do what?
 */
data class OneDoesNotSimplyRuParsedInlineQuery(
    val doWhat: String
) : ParsedInlineQuery {
    override val discriminator: String
        get() = doWhat
}
