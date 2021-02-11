package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer

/**
 * An [ImageMacroGenerator] with caching capabilities.
 */
abstract class CachingImageMacroGenerator<T : ParsedInlineQuery>(
    private val registry: MeterRegistry
) : ParsingImageMacroGenerator<T> {
    private val timer = Timer
        .builder("imgmacrobot.generators")
        .tag("name", this::class.simpleName ?: "unknown")
        .register(registry)
    private val cacheHits = Counter
        .builder("imgmacrobot.generators.cachehits")
        .tag("name", this::class.simpleName ?: "unknown")
        .register(registry)

    /**
     * Return cached result for this query, if any.
     */
    abstract fun cached(parsedInlineQuery: ParsedInlineQuery): InlineQueryResult?

    /**
     * Cache the result for this query.
     */
    abstract fun cache(parsedInlineQuery: ParsedInlineQuery, result: InlineQueryResult)

    override suspend fun generate(inlineQuery: InlineQuery): InlineQueryResult? {
        return parseInlineQuery(inlineQuery)?.let { parsedInlineQuery ->
            cached(parsedInlineQuery)?.also {
                cacheHits.increment()
            } ?: run {
                val sample = Timer.start(registry)

                try {
                    generate(parsedInlineQuery)?.also {
                        cache(parsedInlineQuery, it)
                    }
                } finally {
                    sample.stop(timer)
                }
            }
        }
    }
}
