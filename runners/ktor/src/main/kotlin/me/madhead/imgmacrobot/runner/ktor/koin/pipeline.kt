package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import me.madhead.imgmacrobot.core.ImageMacroGenerationPipeline
import me.madhead.imgmacrobot.core.MeteredImageMacroGenerator
import me.madhead.imgmacrobot.core.generators.IronicPalpatine
import me.madhead.imgmacrobot.core.generators.WhatIfIToldYou
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
                MeteredImageMacroGenerator(
                    IronicPalpatine(
                        Path(get<ApplicationConfig>().property("templates_dir").getString()),
                        get(),
                    ),
                    get<PrometheusMeterRegistry>()
                ),
                MeteredImageMacroGenerator(
                    WhatIfIToldYou(
                        Path(get<ApplicationConfig>().property("templates_dir").getString()),
                        Path(get<ApplicationConfig>().property("fonts_dir").getString()),
                        get(),
                    ),
                    get<PrometheusMeterRegistry>()
                )
            ),
            get(),
        )
    }
}
