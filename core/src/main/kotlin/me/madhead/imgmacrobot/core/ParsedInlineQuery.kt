package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery

/**
 * A result of parsing of [inline query][InlineQuery]. It may have some values extracted to be used in placeholders in image macros or be
 * be completely empty for those [generators][ImageMacroGenerator] who don't have any placeholders.
 */
interface ParsedInlineQuery

/**
 * Default [ParsedInlineQuery] implementation used in cases where no values are extracted from the query
 * (e.g. for generating static macros with no placeholders).
 */
object EmptyParsedInlineQuery : ParsedInlineQuery
