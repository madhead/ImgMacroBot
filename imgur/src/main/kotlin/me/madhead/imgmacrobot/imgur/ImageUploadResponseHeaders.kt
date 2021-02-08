package me.madhead.imgmacrobot.imgur

/**
 * Each Imgur request contains rate limit information in the HTTP response headers.
 *
 * @property postRateLimitLimit total POST credits that are allocated.
 * @property postRateLimitRemaining total POST credits available.
 * @property postRateLimitReset time in seconds until your POST ratelimit is reset.
 * @property rateLimitClientLimit total credits that can be allocated for the application in a day.
 * @property rateLimitClientRemaining total credits remaining for the application in a day.
 * @property rateLimitClientReset unknown header.
 * @property rateLimitUserLimit total credits that can be allocated.
 * @property rateLimitUserRemaining total credits available.
 * @property rateLimitUserReset timestamp (unix epoch) for when the credits will be reset.
 */
data class ImageUploadResponseHeaders(
        val postRateLimitLimit: Int? = null,
        val postRateLimitRemaining: Int? = null,
        val postRateLimitReset: Int? = null,
        val rateLimitClientLimit: Int? = null,
        val rateLimitClientRemaining: Int? = null,
        val rateLimitClientReset: Int? = null,
        val rateLimitUserLimit: Int? = null,
        val rateLimitUserRemaining: Int? = null,
        val rateLimitUserReset: Long? = null,
)
