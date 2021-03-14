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
internal class SomethingOfAScientistTest {
    private val templatesDir = mockk<Path>()
    private val imgur = mockk<Imgur>()
    private val fontCollection = mockk<FontCollection>()
    private val cachedInlineQueryResultDAO = mockk<CachedInlineQueryResultDAO>()
    private val registry = SimpleMeterRegistry()
    private val sut = SomethingOfAScientist(templatesDir, imgur, fontCollection, cachedInlineQueryResultDAO, registry)

    @ParameterizedTest
    @MethodSource("parseInlineQueryParams")
    fun parseInlineQuery(inlineQuery: InlineQuery, parsedInlineQuery: SomethingOfAScientistParsedInlineQuery?) {
        val actual = sut.parseInlineQuery(inlineQuery)

        Assertions.assertEquals(parsedInlineQuery, actual)
    }

    @Suppress("unused", "LongMethod")
    private fun parseInlineQueryParams(): List<Arguments> {
        val user = CommonUser(
            id = ChatId(0),
            firstName = "user"
        )

        return listOf(
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "scientist: mad scientist", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "scientist: \t    mad scientist", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know, I'm something of a scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know, I'm something of scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know, I am something of a scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know, I am something of scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know, I'm something of a mad scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know, I'm something of mad scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "I'm something of a mad scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "I'm something of mad scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "I'm something of a mad scientist", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "I'm something of mad scientist", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Im something of a mad scientist", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Im something of mad scientist", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Im something of a mad scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Im something of mad scientist myself", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Im something of a mad scientist, you know", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "Im something of mad scientist, you know", offset = ""),
                SomethingOfAScientistParsedInlineQuery("mad scientist")
            ),
            Arguments.of(
                BaseInlineQuery(id = "", from = user, query = "You know that I know", offset = ""),
                null
            ),
            Arguments.of(
                BaseInlineQuery(
                    id = "",
                    from = user,
                    query = "I am technology mad. I am putting my screwdriver everywhere and seeing what is happening.",
                    offset = ""
                ),
                null
            ),
        )
    }
}
