package me.madhead.imgmacrobot.runner.ktor

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.routing.routing
import me.madhead.imgmacrobot.runner.ktor.koin.configModule
import me.madhead.imgmacrobot.runner.ktor.koin.imgurModule
import me.madhead.imgmacrobot.runner.ktor.koin.jsonModule
import me.madhead.imgmacrobot.runner.ktor.koin.pipelineModule
import me.madhead.imgmacrobot.runner.ktor.koin.telegramModule
import me.madhead.imgmacrobot.runner.ktor.routes.webhook
import org.koin.ktor.ext.Koin
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
                imgurModule,
                jsonModule,
                telegramModule,
                pipelineModule,
        )
    }

    routing {
        webhook()
    }
}
