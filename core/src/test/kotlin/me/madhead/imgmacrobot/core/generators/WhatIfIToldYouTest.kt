package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.CommonUser
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import dev.inmo.tgbotapi.types.InlineQueries.query.BaseInlineQuery
import io.mockk.mockk
import me.madhead.imgmacrobot.imgur.Imgur
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class WhatIfIToldYouTest {
    private val templatesDir = mockk<Path>()
    private val fontsDir = mockk<Path>()
    private val imgur = mockk<Imgur>()
    private val sut = WhatIfIToldYou(templatesDir, fontsDir, imgur)

    @ParameterizedTest
    @MethodSource("parseInlineQueryParams")
    fun parseInlineQuery(inlineQuery: InlineQuery, parsedInlineQuery: WhatIfIToldYouParsedInlineQuery?) {
        val actual = sut.parseInlineQuery(inlineQuery)

        Assertions.assertEquals(parsedInlineQuery, actual)
    }

    @Suppress("unused")
    private fun parseInlineQueryParams(): List<Arguments> {
        val user = CommonUser(
            id = ChatId(0),
            firstName = "user"
        )

        return listOf(
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "What if I told you that Morpheus never said that", offset = ""),
                WhatIfIToldYouParsedInlineQuery("that Morpheus never said that")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "wHaT   iF i   ToLd yOu that Morpheus never said that", offset = ""),
                WhatIfIToldYouParsedInlineQuery("that Morpheus never said that")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "What if I told you", offset = ""),
                null
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "What if I told you       ", offset = ""),
                null
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "What if I told you…", offset = ""),
                null
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "What if you told me your secret?", offset = ""),
                null
            ),
        )
    }
}
