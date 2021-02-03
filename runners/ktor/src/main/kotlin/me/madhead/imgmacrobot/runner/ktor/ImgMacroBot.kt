package me.madhead.imgmacrobot.runner.ktor

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import me.madhead.imgmacrobot.runner.ktor.koin.jsonModule
import me.madhead.imgmacrobot.runner.ktor.routes.webhook
import org.apache.logging.log4j.LogManager
import org.koin.ktor.ext.Koin

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)
    install(Koin) {
        modules(
                jsonModule,
        )
    }

    routing {
        webhook()
    }
}
