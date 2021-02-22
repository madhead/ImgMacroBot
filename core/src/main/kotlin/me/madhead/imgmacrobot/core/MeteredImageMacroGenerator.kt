package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer

/**
 * A [ParsingImageMacroGenerator] implementation who tracks the timings of its delegates.
 */
class MeteredImageMacroGenerator<T : ParsedInlineQuery>(
        private val delegate: ParsingImageMacroGenerator<T>,
        private val registry: MeterRegistry,
) : ParsingImageMacroGenerator<T> {
    private val timer = Timer
            .builder("imgmacrobot.generators")
            .description("Image macro generation stats")
            .tag("name", delegate::class.simpleName ?: "unknown")
            .register(registry)

    override fun parseInlineQuery(inlineQuery: InlineQuery): T? = delegate.parseInlineQuery(inlineQuery)

    override suspend fun generate(parsedInlineQuery: T): InlineQueryResult? {
        val sample = Timer.start(registry)

        try {
            return delegate.generate(parsedInlineQuery)
        } finally {
            sample.stop(timer)
        }
    }
}
