package me.madhead.imgmacrobot.runner.ktor.koin

import io.ktor.config.ApplicationConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import me.madhead.imgmacrobot.core.ImageMacroGenerationPipeline
import me.madhead.imgmacrobot.core.generators.Artemon
import me.madhead.imgmacrobot.core.generators.FirstTime
import me.madhead.imgmacrobot.core.generators.GoodGoodPalpatine
import me.madhead.imgmacrobot.core.generators.GoodGoodPalpatineRu
import me.madhead.imgmacrobot.core.generators.HelpGenerator
import me.madhead.imgmacrobot.core.generators.IronicPalpatine
import me.madhead.imgmacrobot.core.generators.IronicPalpatineRu
import me.madhead.imgmacrobot.core.generators.NotSureIf
import me.madhead.imgmacrobot.core.generators.OneDoesNotSimply
import me.madhead.imgmacrobot.core.generators.OneDoesNotSimplyRu
import me.madhead.imgmacrobot.core.generators.SayWhatAgain
import me.madhead.imgmacrobot.core.generators.SomethingOfAScientist
import me.madhead.imgmacrobot.core.generators.SomethingOfAScientistRu
import me.madhead.imgmacrobot.core.generators.TakeMyMoney
import me.madhead.imgmacrobot.core.generators.VodochkiNam
import me.madhead.imgmacrobot.core.generators.WhatIfIToldYou
import me.madhead.imgmacrobot.core.generators.WhatIfIToldYouRu
import org.jetbrains.skija.Data
import org.jetbrains.skija.FontMgr
import org.jetbrains.skija.Typeface
import org.jetbrains.skija.paragraph.FontCollection
import org.jetbrains.skija.paragraph.TypefaceFontProvider
import org.koin.dsl.module
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.readBytes

/**
 * [Koin module](https://insert-koin.io/docs/reference/koin-core/modules) for
 * [image macro generation facilities][ImageMacroGenerationPipeline].
 */
@ExperimentalPathApi
val pipelineModule = module {
    single {
        val fontsDir = Path(get<ApplicationConfig>().property("fontsDir").getString())
        val fontPath = fontsDir.resolve("Oswald.ttf")
        val fontData = Data.makeFromBytes(fontPath.readBytes())!!
        val font = Typeface.makeFromData(fontData)
        val fontProvider = TypefaceFontProvider()
        val fontCollection = FontCollection()

        fontProvider.registerTypeface(font)
        fontCollection.setDefaultFontManager(FontMgr.getDefault())
        fontCollection.setAssetFontManager(fontProvider)

        fontCollection
    }

    single {
        ImageMacroGenerationPipeline(
            listOf(
                IronicPalpatine(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                IronicPalpatineRu(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                GoodGoodPalpatine(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                GoodGoodPalpatineRu(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                WhatIfIToldYou(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                WhatIfIToldYouRu(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                SomethingOfAScientist(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                SomethingOfAScientistRu(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                OneDoesNotSimply(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                OneDoesNotSimplyRu(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                VodochkiNam(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                TakeMyMoney(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                SayWhatAgain(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                Artemon(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                NotSureIf(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                FirstTime(
                    Path(get<ApplicationConfig>().property("templatesDir").getString()),
                    get(),
                    get(),
                    get(),
                    get<PrometheusMeterRegistry>()
                ),
                HelpGenerator()
            ),
            get(),
        )
    }
}
