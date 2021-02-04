package me.madhead.imgmacrobot.runner.ktor.koin

import me.madhead.imgmacrobot.core.ImageMacroGenerationPipeline
import org.koin.dsl.module

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [image macro generation facilities][ImageMacroGenerationPipeline].
 */
val pipelineModule = module {
    single {
        ImageMacroGenerationPipeline(
                listOf(
                ),
                get(),
        )
    }
}
