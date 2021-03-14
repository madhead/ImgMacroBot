package me.madhead.imgmacrobot.core.dao

import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResult
import me.madhead.imgmacrobot.core.entity.CachedInlineQueryResultType
import javax.sql.DataSource

/**
 * [CachedInlineQueryResultDAO] implementation for Oracle ATP.
 */
class OracleCachedInlineQueryResultDAO(
    private val dataSource: DataSource
) : CachedInlineQueryResultDAO {
    override suspend fun save(value: CachedInlineQueryResult) {
        dataSource.connection?.use { connection ->
            connection.prepareStatement("""
                MERGE INTO "CACHED_INLINE_QUERY_RESULTS"
                USING DUAL
                ON (("PARSED_INLINE_QUERY_TYPE" = ?) AND ("PARSED_INLINE_QUERY_DISCRIMINATOR" = ?))
                WHEN MATCHED THEN
                    UPDATE
                    SET "TYPE"=?,
                        "URL" = ?,
                        "WIDTH" = ?,
                        "HEIGHT" = ?,
                        "ID" = ?,
                        "DELETE_HASH" = ?
                WHEN NOT MATCHED THEN
                    INSERT ("PARSED_INLINE_QUERY_TYPE",
                            "PARSED_INLINE_QUERY_DISCRIMINATOR",
                            "TYPE",
                            "URL",
                            "WIDTH",
                            "HEIGHT",
                            "ID",
                            "DELETE_HASH")
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent())?.use { statement ->
                statement.setString(@Suppress("MagicNumber") 1, value.parsedInlineQuery::class.simpleName)
                statement.setString(@Suppress("MagicNumber") 2, value.parsedInlineQuery.discriminator)
                statement.setString(@Suppress("MagicNumber") 3, value.type.name)
                statement.setString(@Suppress("MagicNumber") 4, value.url)
                statement.setInt(@Suppress("MagicNumber") 5, value.width)
                statement.setInt(@Suppress("MagicNumber") 6, value.height)
                statement.setString(@Suppress("MagicNumber") 7, value.id)
                statement.setString(@Suppress("MagicNumber") 8, value.deleteHash)
                statement.setString(@Suppress("MagicNumber") 9, value.parsedInlineQuery::class.simpleName)
                statement.setString(@Suppress("MagicNumber") 10, value.parsedInlineQuery.discriminator)
                statement.setString(@Suppress("MagicNumber") 11, value.type.name)
                statement.setString(@Suppress("MagicNumber") 12, value.url)
                statement.setInt(@Suppress("MagicNumber") 13, value.width)
                statement.setInt(@Suppress("MagicNumber") 14, value.height)
                statement.setString(@Suppress("MagicNumber") 15, value.id)
                statement.setString(@Suppress("MagicNumber") 16, value.deleteHash)
                statement.executeUpdate()
            }
        }
    }

    @Suppress("NestedBlockDepth")
    override suspend fun get(key: ParsedInlineQuery): CachedInlineQueryResult? = dataSource.connection?.use { connection ->
        connection.prepareStatement("""
            SELECT "TYPE", "URL", "WIDTH", "HEIGHT", "ID", "DELETE_HASH"
            FROM "CACHED_INLINE_QUERY_RESULTS"
            WHERE "PARSED_INLINE_QUERY_TYPE" = ?
              AND "PARSED_INLINE_QUERY_DISCRIMINATOR" = ?
        """.trimIndent())?.use { statement ->
            statement.setString(1, key::class.simpleName)
            statement.setString(2, key.discriminator)
            statement.executeQuery()?.use { resultSet ->
                if (resultSet.next()) {
                    CachedInlineQueryResult(
                        parsedInlineQuery = key,
                        type = CachedInlineQueryResultType.valueOf(resultSet.getString("TYPE")),
                        url = resultSet.getString("URL"),
                        width = resultSet.getInt("WIDTH"),
                        height = resultSet.getInt("HEIGHT"),
                        id = resultSet.getString("ID"),
                        deleteHash = resultSet.getString("DELETE_HASH"),
                    )
                } else {
                    null
                }
            }
        }
    }
}
