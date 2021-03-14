package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultPhotoImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResult
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResultType
import java.util.UUID

/**
 * An [ImageMacroGenerator] with caching capabilities.
 */
abstract class CachingImageMacroGenerator<T : ParsedInlineQuery>(
    private val cachedInlineQueryResultDAO: CachedInlineQueryResultDAO,
    private val registry: MeterRegistry,
) : ParsingImageMacroGenerator<T> {
    private val timer = Timer
        .builder("imgmacrobot.generators")
        .tag("name", this::class.simpleName ?: "unknown")
        .register(registry)
    private val cacheHits = Counter
        .builder("imgmacrobot.generators.cachehits")
        .tag("name", this::class.simpleName ?: "unknown")
        .register(registry)

    override suspend fun generate(parsedInlineQuery: T): InlineQueryResult? {
        return cached(parsedInlineQuery)?.also {
            cacheHits.increment()
        } ?: run {
            val sample = Timer.start(registry)

            try {
                generateCacheable(parsedInlineQuery)?.also {
                    cache(parsedInlineQuery, it)
                }?.inlineQueryResult
            } finally {
                sample.stop(timer)
            }
        }
    }

    /**
     * Generate cacheable result.
     */
    abstract suspend fun generateCacheable(parsedInlineQuery: T): CacheableInlineQueryResult?

    /**
     * Return cached result for this query, if any.
     */
    private suspend fun cached(parsedInlineQuery: T): InlineQueryResult? = cachedInlineQueryResultDAO.get(parsedInlineQuery)?.let {
        when (it.type) {
            CachedInlineQueryResultType.PHOTO -> {
                InlineQueryResultPhotoImpl(
                    id = UUID.randomUUID().toString(),
                    url = it.url,
                    thumbUrl = it.url,
                    width = it.width,
                    height = it.height,
                )
            }
        }
    }

    /**
     * Cache the result for this query.
     */
    private suspend fun cache(parsedInlineQuery: T, result: CacheableInlineQueryResult) = cachedInlineQueryResultDAO.save(
        CachedInlineQueryResult(
            parsedInlineQuery = parsedInlineQuery,
            type = result.type,
            url = result.url,
            width = result.width,
            height = result.height,
            id = result.id,
            deleteHash = result.deleteHash
        )
    )
}
