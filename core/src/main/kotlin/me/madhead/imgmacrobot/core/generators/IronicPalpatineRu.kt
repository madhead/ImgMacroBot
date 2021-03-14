package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.MeterRegistry
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.StaticImageMacroGenerator
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.imgur.Imgur
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi

/**
 * Palpatine's reaction to the tragedy of Darth Plagueis the Wise (russian).
 */
@ExperimentalPathApi
class IronicPalpatineRu(
    templatesDir: Path,
    imgur: Imgur,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : StaticImageMacroGenerator<IronicPalpatineRuParsedInlineQuery>(templatesDir,
    "ironic_ru.jpeg",
    imgur,
    cachedInlineQueryResultDAO,
    registry
) {
    override fun parseInlineQuery(inlineQuery: InlineQuery): IronicPalpatineRuParsedInlineQuery? {
        return if (inlineQuery.query.isNotBlank() &&
            ((inlineQuery.query.contains("иронично", ignoreCase = true)) || ("иронично".contains(inlineQuery.query, ignoreCase = true)))) {
            IronicPalpatineRuParsedInlineQuery
        } else {
            null
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [IronicPalpatineRu].
 */
object IronicPalpatineRuParsedInlineQuery : ParsedInlineQuery {
    override val discriminator: String
        get() = "_"
}
