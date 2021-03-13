package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import me.madhead.imgmacrobot.core.dao.CacheDAO
import me.madhead.imgmacrobot.core.dao.OracleCacheDAO
import me.madhead.imgmacrobot.core.dao.OracleTimestampDAO
import me.madhead.imgmacrobot.core.dao.TimestampDAO
import oracle.ucp.jdbc.PoolDataSourceFactory
import org.koin.dsl.module
import javax.sql.DataSource

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for DAOs.
 */
val dbModule = module {
    single<DataSource> {
        PoolDataSourceFactory.getPoolDataSource()!!.apply {
            val tnsName = get<ApplicationConfig>().property("db.tnsName").getString()
            val tnsAdmin = get<ApplicationConfig>().property("db.tnsAdmin").getString()

            connectionFactoryClassName = "oracle.jdbc.pool.OracleDataSource"
            url = "jdbc:oracle:thin:@$tnsName?TNS_ADMIN=$tnsAdmin"
            user = get<ApplicationConfig>().property("db.user").getString()
            password = get<ApplicationConfig>().property("db.password").getString()
            connectionPoolName = "UCP"
            initialPoolSize = @Suppress("MagicNumber") 5
            minPoolSize = @Suppress("MagicNumber") 5
            maxPoolSize = @Suppress("MagicNumber") 10
        }
    }

    single<TimestampDAO> {
        OracleTimestampDAO(get())
    }

    single<CacheDAO> {
        OracleCacheDAO(get())
    }
}
