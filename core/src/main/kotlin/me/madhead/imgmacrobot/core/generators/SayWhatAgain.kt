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
 * Jules double dares Brett to say "what" again.
 */
@ExperimentalPathApi
class SayWhatAgain(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<SayWhatAgainParsedInlineQuery>(
    templatesDir,
    "say what again.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex =
            "((say what again)|(i (double )?dare you)):(\\s*)(?<top>.+?)(\\s*),(\\s*)(?<bottom>.+?)"
                .toRegex(RegexOption.IGNORE_CASE)
    }

    @Suppress("ReturnCount")
    override fun parseInlineQuery(inlineQuery: InlineQuery): SayWhatAgainParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.let { groups ->
            SayWhatAgainParsedInlineQuery(
                groups["top"]?.value ?: return null,
                groups["bottom"]?.value ?: return null,
            )
        }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: SayWhatAgainParsedInlineQuery) {
        imageMacroParagraph(parsedInlineQuery.top.toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph(parsedInlineQuery.bottom.toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [SayWhatAgain].
 *
 * @property top top text.
 * @property bottom bottom text.
 */
data class SayWhatAgainParsedInlineQuery(
    val top: String,
    val bottom: String,
) : ParsedInlineQuery {
    override val discriminator: String
        get() = "$top/$bottom"
}
