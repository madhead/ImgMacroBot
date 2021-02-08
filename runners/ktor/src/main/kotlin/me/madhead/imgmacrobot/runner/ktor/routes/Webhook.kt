package me.madhead.imgmacrobot.runner.ktor.routes

import dev.inmo.tgbotapi.types.update.abstracts.UpdateDeserializationStrategy
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.post
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.LogManager
import org.koin.ktor.ext.inject

/**
 * [Telegram Bot API webhooks](https://core.telegram.org/bots/api#setwebhook) handler.
 */
fun Route.webhook() {
    val logger = LogManager.getLogger("me.madhead.imgmacrobot.runner.ktor.routes.Webhook")
    val json by inject<Json>()

    post(application.environment.config.property("telegram.token").getString()) {
        try {
            val payload = call.receiveText()

            logger.debug("Request payload: {}", payload)

            val update = json.decodeFromString(UpdateDeserializationStrategy, payload)

            logger.info("Update object: {}", update)
        } catch (ignored: Exception) {
            logger.error("Failed to handle the request", ignored)
        }

        call.respond(HttpStatusCode.OK)
    }
}
