package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import me.madhead.imgmacrobot.core.ImageMacroGenerator

/**
 * Not for productions usage: this [ImageMacroGenerator] always throws an error when trying to generate a result.
 * It may be useful for testing.
 */
@Suppress("unused")
class FaultyImageMacroGenerator : ImageMacroGenerator {
    override suspend fun generate(inlineQuery: InlineQuery): InlineQueryResult? {
        throw IllegalArgumentException()
    }
}
