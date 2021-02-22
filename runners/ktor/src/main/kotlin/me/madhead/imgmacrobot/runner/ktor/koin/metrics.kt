package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [Micrometer Metrics](https://ktor.io/docs/micrometer-metrics.html).
 */
val metricsModule = module {
    single {
        val token = get<ApplicationConfig>().property("telegram.token").getString()

        PrometheusMeterRegistry(PrometheusConfig.DEFAULT).also {
            it.config().commonTags("application", "ImgMacroBot")
            it.config().meterFilter(MeterFilter.replaceTagValues("route", { value ->
                if (value?.contains(token) == true) {
                    "/webhook"
                } else {
                    value
                }
            }))
        }
    }
}
