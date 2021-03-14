package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResultType

/**
 * [InlineQueryResult] plus some metadata for caching.
 *
 * @property inlineQueryResult cacheable value.
 * @property type [InlineQueryResult] type.
 * @property url resource url.
 * @property width resource width.
 * @property height resource height.
 * @property id resource id.
 * @property deleteHash secret token used to manage the resource.
 */
data class CacheableInlineQueryResult(
    val inlineQueryResult: InlineQueryResult,

    val type: CachedInlineQueryResultType,
    val url: String,
    val width: Int,
    val height: Int,
    val id: String,
    val deleteHash: String,
)
