package me.madhead.imgmacrobot.core

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.answers.answerInlineQuery
import dev.inmo.tgbotapi.types.update.InlineQueryUpdate
import dev.inmo.tgbotapi.types.update.abstracts.Update
import org.apache.logging.log4j.LogManager

/**
 * [Telegram's Bot API updates](https://core.telegram.org/bots/api#getting-updates) processor. It responds with a list of generated image
 * macros for incoming [inline queries](https://core.telegram.org/bots/inline) and filters out all other updates.
 */
class ImageMacroGenerationPipeline(
        private val generators: List<ImageMacroGenerator>,
        private val requestsExecutor: RequestsExecutor,
) {
    companion object {
        private val logger = LogManager.getLogger(ImageMacroGenerationPipeline::class.java)!!
    }

    /**
     * Process an incoming [update].
     */
    suspend fun process(update: Update) {
        logger.debug("Processing update: {}", update)

        if (update !is InlineQueryUpdate) {
            logger.info("Update is not an InlineQuery!")

            return
        }

        logger.info("Inline query: {}", update.data)

        val a = requestsExecutor.answerInlineQuery(
                inlineQuery = update.data,
                results = generators.mapNotNull { it.generate(update.data) }
        )

        logger.info("Result: {}", a)
    }
}
