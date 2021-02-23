package me.madhead.imgmacrobot.runner.ktor.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.inject

/**
 * [Prometheus scrape endpoint](https://ktor.io/docs/micrometer-metrics.html#prometheus_endpoint).
 */
fun Route.metrics() {
    val registry by inject<PrometheusMeterRegistry>()

    get("/metrics") {
        call.respond(registry.scrape())
    }
}
