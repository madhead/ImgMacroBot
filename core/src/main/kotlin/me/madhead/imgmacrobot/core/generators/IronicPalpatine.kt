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
 * Palpatine's reaction to the tragedy of Darth Plagueis the Wise.
 */
@ExperimentalPathApi
class IronicPalpatine(
    templatesDir: Path,
    imgur: Imgur,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : StaticImageMacroGenerator<IronicPalpatineParsedInlineQuery>(templatesDir,
    "ironic.jpeg",
    imgur,
    cachedInlineQueryResultDAO,
    registry
) {
    override fun parseInlineQuery(inlineQuery: InlineQuery): IronicPalpatineParsedInlineQuery? {
        return if (inlineQuery.query.isNotBlank() &&
            ((inlineQuery.query.contains("ironic", ignoreCase = true)) || ("ironic".contains(inlineQuery.query, ignoreCase = true)))) {
            IronicPalpatineParsedInlineQuery
        } else {
            null
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [IronicPalpatine].
 */
object IronicPalpatineParsedInlineQuery : ParsedInlineQuery {
    override val discriminator: String
        get() = "_"
}
