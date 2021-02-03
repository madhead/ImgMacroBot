package me.madhead.imgmacrobot.runner.ktor.koin

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val jsonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
}
