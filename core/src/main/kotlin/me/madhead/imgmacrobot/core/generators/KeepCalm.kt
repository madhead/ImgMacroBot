package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.MeterRegistry
import me.madhead.imgmacrobot.core.CacheableInlineQueryResult
import me.madhead.imgmacrobot.core.CachingImageMacroGenerator
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.imgur.Imgur
import org.jetbrains.skija.paragraph.FontCollection
import kotlin.io.path.ExperimentalPathApi

/**
 * Morpheus from the "The Matrix" trying to tell you something.
 */
@ExperimentalPathApi
class KeepCalm(
    imgur: Imgur,
    fontCollection: FontCollection,
    cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    registry: MeterRegistry,
) : CachingImageMacroGenerator<KeepCalmParsedInlineQuery>(
    cachedInlineQueryResultDAO,
    registry,
) {
    companion object {
        private val macroIdRegex =
            "keep calm:(\\s*)(?<doWhat>.+?)(\\s*),(\\s*)(?<bg>.+?)"
                .toRegex(RegexOption.IGNORE_CASE)
        private val regex =
            "(?<top>.+ принеси)(\\s*)\\p{P}?(\\s*)(?<bottom>.+)"
                .toRegex(RegexOption.IGNORE_CASE)
    }

    override fun parseInlineQuery(inlineQuery: InlineQuery): KeepCalmParsedInlineQuery? {
        TODO("Not yet implemented")
    }

    override suspend fun generateCacheable(parsedInlineQuery: KeepCalmParsedInlineQuery): CacheableInlineQueryResult? {
        TODO("Not yet implemented")
    }
}

/**
 * [ParsedInlineQuery] implementation for [KeepCalm].
 *
 * @property doWhat keep calm and do what?
 * @property bg background color
 * @property fg foreground color
 */
data class KeepCalmParsedInlineQuery(
    val doWhat: String,
    val bg: Int = 0xFFFB0F25.toInt(),
    val fg: Int = 0xFFFFFFFF.toInt(),
) : ParsedInlineQuery {
    override val discriminator: String
        get() = "$doWhat/$bg/$fg"
}
