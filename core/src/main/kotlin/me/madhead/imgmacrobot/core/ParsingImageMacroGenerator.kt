package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.InlineQueryResult
import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery

/**
 * An [ImageMacroGenerator] who parses the [inline query][InlineQuery], extracting some values from it. Even if the image macro has no
 * placeholders (i.e. doesn't need any values to be extracted from the inline query) parsing could be used to indicate that a generator is
 * willing to offer some content by returning [non-null result][ParsedInlineQuery] (i.e. [EmptyParsedInlineQuery]). Finally,
 * when [parseInlineQuery] returns null, it means that a generator doesn't support this inline query and is not willing to generate
 * any content. It effectively means that its [generate] will not be called.
 */
interface ParsingImageMacroGenerator<T : ParsedInlineQuery> : ImageMacroGenerator {
    /**
     * Parse [inlineQuery] into a [data holder][ParsedInlineQuery] accepted later by [generate]. Return null if this generato is not
     * willing to process this inline query. It effectively means that its [generate] will not be called.
     */
    fun parseInlineQuery(inlineQuery: InlineQuery): T?

    /**
     * Basically, the same as [ImageMacroGenerator.generate], but accepts a typed [data holder][ParsedInlineQuery] who may contain values
     * extracted from the inline query to be used in placeholders in image macro.
     */
    suspend fun generate(parsedInlineQuery: T): InlineQueryResult?

    override suspend fun generate(inlineQuery: InlineQuery): InlineQueryResult? = parseInlineQuery(inlineQuery)?.let { generate(it) }
}
