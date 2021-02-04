package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery

/**
 * Each incoming [inline query](https://core.telegram.org/bots/inline) is passed through all registered [ImageMacroGenerator]
 * implementations, each of them may or may not return some content in a form of [InlineQueryResult].
 */
interface ImageMacroGenerator {
    /**
     * Implementations must return null if they don't have any content to offer in response to the given [inlineQuery] or
     * [InlineQueryResult] implementation otherwise.
     */
    suspend fun generate(inlineQuery: InlineQuery): InlineQueryResult?
}
