package me.madhead.imgmacrobot.runner.ktor.koin

import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [Micrometer Metrics](https://ktor.io/docs/micrometer-metrics.html).
 */
val metricsModule = module {
    single {
        PrometheusMeterRegistry(PrometheusConfig.DEFAULT).also {
            it.config().commonTags("application", "ImgMacroBot")
        }
    }
}
