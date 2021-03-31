package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.answers.answerInlineQuery
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.types.ParseMode.MarkdownV2
import dev.inmo.tgbotapi.types.update.InlineQueryUpdate
import dev.inmo.tgbotapi.types.update.MessageUpdate
import dev.inmo.tgbotapi.types.update.abstracts.Update
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout
import org.apache.logging.log4j.LogManager

/**
 * [Telegram's Bot API updates](https://core.telegram.org/bots/api#getting-updates) processor. It responds with a list of generated image
 * macros for incoming [inline queries](https://core.telegram.org/bots/inline) and filters out all other updates.
 *
 * @property generators a list of registered [image macro generators][ImageMacroGenerator].
 * @property requestsExecutor Telegram Bot API.
 * @property timeout maximum time given to a single generator to provide any results in milliseconds.
 */
class ImageMacroGenerationPipeline(
    private val generators: List<ImageMacroGenerator>,
    private val requestsExecutor: RequestsExecutor,
    private val timeout: Long = 5_000
) {
    companion object {
        private val logger = LogManager.getLogger(ImageMacroGenerationPipeline::class.java)!!
    }

    /**
     * Process an incoming [update].
     */
    suspend fun process(update: Update) {
        logger.debug("Processing update: {}", update)

        if (update is MessageUpdate) {
            help(update)
        }

        if (update !is InlineQueryUpdate) {
            logger.info("Update is not an InlineQuery!")

            return
        }

        logger.info("Inline query: {}", update.data)

        val results = coroutineScope {
            generators
                .map { generator ->
                    async {
                        try {
                            withTimeout(timeout) {
                                generator.generate(update.data)
                            }
                        } catch (ignored: Throwable) {
                            logger.error("{} failed!", generator::class.simpleName, ignored)

                            null
                        }
                    }
                }
                .awaitAll()
                .filterNotNull()
        }

        logger.info("Inline query results: {}", results)

        requestsExecutor.answerInlineQuery(
            inlineQuery = update.data,
            results = results
        )
    }

    private suspend fun help(update: MessageUpdate) {
        requestsExecutor.reply(
            to = update.data,
            text = "This is an [inline bot](https://core.telegram.org/bots/inline)\\. " +
                "Don't send messages to it directly\\. " +
                "Instead, mention it in a chat, providing a query, like `@ImgMacroBot not sure if <A>, or just <B>`\\.",
            parseMode = MarkdownV2,
            disableWebPagePreview = true,
        )
    }
}
