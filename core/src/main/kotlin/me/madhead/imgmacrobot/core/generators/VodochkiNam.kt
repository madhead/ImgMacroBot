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
 * Malchik, vodochki nam prinesi!
 */
@ExperimentalPathApi
class VodochkiNam(
    templatesDir: Path,
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : ParagraphsImageMacroGenerator<VodochkiNamParsedInlineQuery>(
    templatesDir,
    "vodochki.png",
    imgur,
    fontCollection,
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex =
            "водочки:(\\s*)(?<bringWhat>.+?)(\\s*),(\\s*)(?<whom>.+?)(\\s*),(\\s*)(?<who>.+?)(\\s*),(\\s*)(?<doWhat>.+)"
                .toRegex(RegexOption.IGNORE_CASE)
        private val regex =
            "((.*ты не понял\\p{P}(\\s+))++)?(?<bringWhat>.+?)(\\s+)(?<whom>.+?)(\\s+)принеси,?(\\s+)(?<who>.+?) (?<doWhat>.+)"
                .toRegex(RegexOption.IGNORE_CASE)
    }

    @Suppress("ReturnCount")
    override fun parseInlineQuery(inlineQuery: InlineQuery): VodochkiNamParsedInlineQuery? {
        return macroIdRegex.matchEntire(inlineQuery.query)?.groups?.let { groups ->
            VodochkiNamParsedInlineQuery(
                groups["bringWhat"]?.value ?: return null,
                groups["whom"]?.value ?: return null,
                groups["who"]?.value ?: return null,
                groups["doWhat"]?.value ?: return null,
            )
        }
            ?: regex.matchEntire(inlineQuery.query)?.groups?.let { groups ->
                VodochkiNamParsedInlineQuery(
                    groups["bringWhat"]?.value ?: return null,
                    groups["whom"]?.value ?: return null,
                    groups["who"]?.value ?: return null,
                    groups["doWhat"]?.value ?: return null,
                )
            }
    }

    override fun drawParagraphs(template: Image, canvas: Canvas, parsedInlineQuery: VodochkiNamParsedInlineQuery) {
        imageMacroParagraph("${parsedInlineQuery.bringWhat} ${parsedInlineQuery.whom} ПРИНЕСИ".toUpperCase()) {
            layout(template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0F, @Suppress("MagicNumber") 10F)
        }
        imageMacroParagraph("${parsedInlineQuery.who} ${parsedInlineQuery.doWhat}".toUpperCase()) {
            layout(@Suppress("MagicNumber") 0.9F * template.width.toFloat())
            paint(canvas, @Suppress("MagicNumber") 0.05F * template.width, template.height - @Suppress("MagicNumber") 10 - height)
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [VodochkiNam].
 *
 * @property bringWhat what has the malchik to bring us?
 * @property whom whom has the malchink to bring some vodochka?
 * @property who who does something extraordinary so that the malchik has to bring them some vodochka?
 * @property doWhat what do we do, so that the malchik has to bring us some vodochka?
 */
data class VodochkiNamParsedInlineQuery(
    val bringWhat: String,
    val whom: String,
    val who: String,
    val doWhat: String,
) : ParsedInlineQuery {
    override val discriminator: String
        get() = "$bringWhat/$whom/$who/$doWhat"
}
