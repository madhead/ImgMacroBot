package me.madhead.imgmacrobot.core.entity

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import me.madhead.imgmacrobot.core.ParsedInlineQuery

/**
 * Cached image macro generation result.
 *
 * @property parsedInlineQuery cache key.
 * @property type [InlineQueryResult] type.
 * @property url resource url.
 * @property width resource width.
 * @property height resource height.
 * @property id resource id.
 * @property deleteHash secret token used to manage the resource.
 */
data class CachedInlineQueryResult(
    val parsedInlineQuery: ParsedInlineQuery,
    val type: CachedInlineQueryResultType,
    val url: String,
    val width: Int,
    val height: Int,
    val id: String,
    val deleteHash: String,
)

/**
 * Cached image macro generation result type.
 */
enum class CachedInlineQueryResultType {
    PHOTO
}
