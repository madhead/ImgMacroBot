package me.madhead.imgmacrobot.core.dao

/**
 * Oracle [drops](https://docs.oracle.com/en/cloud/paas/autonomous-database/adbsa/autonomous-always-free.html) inactive ATPs after some
 * time. So, this DAO writes timestamps into the database to prevent that.
 */
interface TimestampDAO {
    /**
     * Update something in the database so it's not stopped and dropped by Oracle.
     */
    suspend fun update()
}
