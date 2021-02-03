package me.madhead.imgmacrobot.runner.ktor.koin

import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [JSON serialization](https://github.com/Kotlin/kotlinx.serialization).
 */
val jsonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
}
