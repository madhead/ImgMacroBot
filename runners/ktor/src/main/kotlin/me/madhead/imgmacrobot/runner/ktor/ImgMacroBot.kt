package me.madhead.imgmacrobot.runner.ktor

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.config.ApplicationConfig
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.request.path
import io.ktor.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry
import me.madhead.imgmacrobot.runner.ktor.koin.configModule
import me.madhead.imgmacrobot.runner.ktor.koin.imgurModule
import me.madhead.imgmacrobot.runner.ktor.koin.jsonModule
import me.madhead.imgmacrobot.runner.ktor.koin.metricsModule
import me.madhead.imgmacrobot.runner.ktor.koin.pipelineModule
import me.madhead.imgmacrobot.runner.ktor.koin.telegramModule
import me.madhead.imgmacrobot.runner.ktor.routes.metrics
import me.madhead.imgmacrobot.runner.ktor.routes.webhook
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import kotlin.io.path.ExperimentalPathApi

/**
 * Main [Ktor module](https://ktor.io/docs/modules.html).
 */
@ExperimentalPathApi
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)
    install(Koin) {
        modules(
                configModule(environment.config),
                metricsModule,
                imgurModule,
                jsonModule,
                telegramModule,
                pipelineModule,
        )
    }
    install(MicrometerMetrics) {
        registry = get<PrometheusMeterRegistry>()

        val token = get<ApplicationConfig>().property("telegram.token").getString()

        // Hide Telegram Bot token
        timers { call, _ ->
            if (call.request.path().contains(token)) {
                tag("route", "/webhook")
            }
        }
    }

    routing {
        webhook()
        metrics()
    }
}
