package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import me.madhead.imgmacrobot.core.ImageMacroGenerationPipeline
import me.madhead.imgmacrobot.core.generators.IronicPalpatine
import org.koin.dsl.module
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [image macro generation facilities][ImageMacroGenerationPipeline].
 */
@ExperimentalPathApi
val pipelineModule = module {
    single {
        ImageMacroGenerationPipeline(
                listOf(
                        IronicPalpatine(
                                Path(get<ApplicationConfig>().property("templates_dir").getString()),
                                get(),
                        ),
                ),
                get(),
        )
    }
}
