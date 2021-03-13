package me.madhead.imgmacrobot.runner.ktor

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.madhead.imgmacrobot.core.dao.TimestampDAO
import me.madhead.imgmacrobot.runner.ktor.koin.configModule
import me.madhead.imgmacrobot.runner.ktor.koin.dbModule
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
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

/**
 * Main [Ktor module](https://ktor.io/docs/modules.html).
 */
@ExperimentalTime
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
            dbModule,
            pipelineModule,
        )
    }
    install(MicrometerMetrics) {
        registry = get<PrometheusMeterRegistry>()
        meterBinders = listOf(
            ClassLoaderMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics(),
            JvmThreadMetrics(),
            FileDescriptorMetrics(),
            UptimeMetrics(),
        )
    }

    routing {
        webhook()
        metrics()
    }

    launch {
        val timestampDAO = get<TimestampDAO>()

        while (true) {
            try {
                timestampDAO.update()
            } catch (ignored: Exception) {
                log.error("Failed to write the latest timestamp", ignored)
            }
            delay(1.minutes)
        }
    }
}
