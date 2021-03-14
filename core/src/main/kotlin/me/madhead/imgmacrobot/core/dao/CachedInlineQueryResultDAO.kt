package me.madhead.imgmacrobot.core.dao

import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResult

/**
 * Inline query results cache.
 */
interface CachedInlineQueryResultDAO {
    /**
     * Save the value in the database.
     */
    suspend fun save(value: CachedInlineQueryResult)

    /**
     * Get the value from the database.
     */
    suspend fun get(key: ParsedInlineQuery): CachedInlineQueryResult?
}
