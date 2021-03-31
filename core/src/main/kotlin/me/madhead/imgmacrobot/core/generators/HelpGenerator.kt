package me.madhead.imgmacrobot.core.generators

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultArticle
import dev.inmo.tgbotapi.types.InlineQueries.InputMessageContent.InputTextMessageContent
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery
import dev.inmo.tgbotapi.types.ParseMode.MarkdownV2
import me.madhead.imgmacrobot.core.ParsedInlineQuery
import me.madhead.imgmacrobot.core.ParsingImageMacroGenerator
import java.util.UUID

/**
 * A link to the README on GitHub.
 */
class HelpGenerator : ParsingImageMacroGenerator<HelpGeneratorParsedInlineQuery> {
    override fun parseInlineQuery(inlineQuery: InlineQuery): HelpGeneratorParsedInlineQuery? {
        return if (inlineQuery.query.isNotBlank() &&
            ((inlineQuery.query.contains("help", ignoreCase = true)) || ("help".contains(inlineQuery.query, ignoreCase = true)))) {
            HelpGeneratorParsedInlineQuery
        } else {
            null
        }
    }

    override suspend fun generate(parsedInlineQuery: HelpGeneratorParsedInlineQuery) = InlineQueryResultArticle(
        id = UUID.randomUUID().toString(),
        title = "ImgMacroBot help",
        inputMessageContent = InputTextMessageContent(
            text = "[ImgMacroBot help](https://github.com/madhead/ImgMacroBot#usage)",
            parseMode = MarkdownV2
        ),
        url = "https://github.com/madhead/ImgMacroBot#usage",
    )
}

/**
 * [ParsedInlineQuery] implementation for [HelpGenerator].
 */
object HelpGeneratorParsedInlineQuery : ParsedInlineQuery {
    override val discriminator: String
        get() = "_"
}
