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
 * Palpatine's reaction to the hate flowing through Luke Skywalker.
 */
@ExperimentalPathApi
class GoodGoodPalpatineRu(
    templatesDir: Path,
    imgur: Imgur,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : StaticImageMacroGenerator<GoodGoodPalpatineRuParsedInlineQuery>(templatesDir,
    "good good ru.jpeg",
    imgur,
    cachedInlineQueryResultDAO,
    registry
) {
    companion object {
        private val regex = "хо+ро+шо+".toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): GoodGoodPalpatineRuParsedInlineQuery? {
        return if (regex.containsMatchIn(inlineQuery.query)) {
            GoodGoodPalpatineRuParsedInlineQuery
        } else {
            null
        }
    }
}

/**
 * [ParsedInlineQuery] implementation for [GoodGoodPalpatineRu].
 */
object GoodGoodPalpatineRuParsedInlineQuery : ParsedInlineQuery {
    override val discriminator: String
        get() = "_"
}
