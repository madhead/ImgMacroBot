package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import me.madhead.imgmacrobot.imgur.Imgur
import me.madhead.imgmacrobot.imgur.ktor.KtorImgur
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) [Imgur](https://imgur.com) client.
 */
val imgurModule = module {
    single<Imgur> {
        KtorImgur(
            get<ApplicationConfig>().property("imgur.clientId").getString(),
            get(named(IMGUR_RATE_LIMITS)),
        )
    }
}
