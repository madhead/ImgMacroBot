package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.CommonUser
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import dev.inmo.tgbotapi.types.InlineQueries.query.BaseInlineQuery
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.mockk.mockk
import me.madhead.imgmacrobot.core.dao.CachedInlineQueryResultDAO
import me.madhead.imgmacrobot.imgur.Imgur
import org.jetbrains.skija.paragraph.FontCollection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NotSureIfTest {
    private val templatesDir = mockk<Path>()
    private val imgur = mockk<Imgur>()
    private val fontCollection = mockk<FontCollection>()
    private val cachedInlineQueryResultDAO = mockk<CachedInlineQueryResultDAO>()
    private val registry = SimpleMeterRegistry()
    private val sut = NotSureIf(templatesDir, imgur, fontCollection, cachedInlineQueryResultDAO, registry)

    @ParameterizedTest
    @MethodSource("parseInlineQueryParams")
    fun parseInlineQuery(inlineQuery: InlineQuery, parsedInlineQuery: NotSureIfParsedInlineQuery?) {
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
                BaseInlineQuery(id = "", from = user, query = "not sure: Trolling, or just stupid", offset = ""),
                NotSureIfParsedInlineQuery("Trolling", "or just stupid")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "not sure if: Trolling, or just stupid", offset = ""),
                NotSureIfParsedInlineQuery("Trolling", "or just stupid")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Not sure if trolling, or just stupid", offset = ""),
                NotSureIfParsedInlineQuery("Not sure if trolling", "or just stupid")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Молодой человек, мы, русские, не обманываем друг друга!", offset = ""),
                null
            ),
        )
    }
}
