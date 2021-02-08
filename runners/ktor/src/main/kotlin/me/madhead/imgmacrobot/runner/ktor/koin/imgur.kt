package me.madhead.imgmacrobot.runner.ktor.koin

import me.madhead.imgmacrobot.imgur.Imgur
import me.madhead.imgmacrobot.imgur.ktor.KtorImgur
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) [Imgur](https://imgur.com) client.
 */
val imgurModule = module {
    single<Imgur> {
        KtorImgur()
    }
}
