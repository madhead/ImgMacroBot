package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import kotlinx.coroutines.time.delay
import me.madhead.imgmacrobot.core.ImageMacroGenerator
import java.time.Duration

/**
 * Not for productions usage: this [ImageMacroGenerator] takes too long to generate a result.
 * It may be useful for testing.
 */
@Suppress("unused")
class LingeringImageMacroGenerator : ImageMacroGenerator {
    override suspend fun generate(inlineQuery: InlineQuery): InlineQueryResult? {
        delay(Duration.ofHours(1))
        return null
    }
}
