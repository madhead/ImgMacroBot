package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.types.InlineQueries.abstracts.InlineQuery

/**
 * A result of parsing of [inline query][InlineQuery]. It may have some values extracted to be used in placeholders in image macros or be
 * be completely empty for those [generators][ImageMacroGenerator] who don't have any placeholders.
 */
interface ParsedInlineQuery {
    /**
     * A value used to differentiate parsed inline queries of the same type. Something like combined `equals` and `hashCode`: it's a simple
     * value like `hashCode`, but must be stable across different VM instances (`hashCode` are not, generally), and it could be used to
     * check the equality as well.
     */
    val discriminator: String
}
