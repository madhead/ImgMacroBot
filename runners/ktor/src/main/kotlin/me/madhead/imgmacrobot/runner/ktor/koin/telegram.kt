package me.madhead.imgmacrobot.runner.ktor.koin

import dev.inmo.tgbotapi.extensions.api.telegramBot
import io.ktor.config.ApplicationConfig
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for Telegram Bot API.
 */
val telegramModule = module {
    single {
        telegramBot(get<ApplicationConfig>().property("telegram.token").getString())
    }
}
