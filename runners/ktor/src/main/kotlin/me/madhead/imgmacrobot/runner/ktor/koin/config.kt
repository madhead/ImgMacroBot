package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [Ktor configuration](https://ktor.io/docs/configurations.html#accessing-config).
 */
fun configModule(config: ApplicationConfig) = module {
    single {
        config
    }
}
