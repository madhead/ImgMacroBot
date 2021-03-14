package me.madhead.imgmacrobot.core.dao

import java.sql.Timestamp
import java.time.Instant
import javax.sql.DataSource

/**
 * [TimestampDAO] implementation for Oracle ATP.
 */
class OracleTimestampDAO(
    private val dataSource: DataSource
) : TimestampDAO {
    override suspend fun update() {
        dataSource.connection?.use { connection ->
            connection.prepareStatement("""
                MERGE INTO TIMESTAMP
                USING DUAL
                ON ("ID" = 'LATEST')
                WHEN MATCHED THEN
                    UPDATE
                    SET "VALUE"=?
                WHEN NOT MATCHED THEN
                    INSERT ("ID", "VALUE")
                    VALUES ('LATEST', ?)
            """.trimIndent())?.use { statement ->
                val timestamp = Timestamp.from(Instant.now())

                statement.setTimestamp(1, timestamp)
                statement.setTimestamp(2, timestamp)
                statement.executeUpdate()
            }
        }
    }
}
