package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.CommonUser
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import dev.inmo.tgbotapi.types.InlineQueries.query.BaseInlineQuery
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.mockk.mockk
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
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
internal class IronicPalpatineTest {
    private val templatesDir = mockk<Path>()
    private val imgur = mockk<Imgur>()
    private val cachedInlineQueryResultDAO = mockk<CachedInlineQueryResultDAO>()
    private val registry = SimpleMeterRegistry()
    private val sut = IronicPalpatine(templatesDir, imgur, cachedInlineQueryResultDAO, registry)

    @ParameterizedTest
    @MethodSource("parseInlineQueryParams")
    fun parseInlineQuery(inlineQuery: InlineQuery, parsedInlineQuery: IronicPalpatineParsedInlineQuery?) {
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
                BaseInlineQuery(id = "", from = user, query = "ironic", offset = ""),
                IronicPalpatineParsedInlineQuery
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "ironi", offset = ""),
                IronicPalpatineParsedInlineQuery
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "ironic situation", offset = ""),
                IronicPalpatineParsedInlineQuery
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "situation is ironic", offset = ""),
                IronicPalpatineParsedInlineQuery
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "", offset = ""),
                null
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "execute order 66", offset = ""),
                null
            ),
        )
    }
}
