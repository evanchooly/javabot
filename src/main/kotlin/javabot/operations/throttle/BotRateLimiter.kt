package javabot.operations.throttle

class BotRateLimiter(private val maxRequests: Int, private val timeWindowMillis: Long) {
    private val requests = mutableListOf<Long>()

    @Synchronized
    fun tryAcquire(): Boolean {
        val now = System.currentTimeMillis()

        // Remove expired entries
        while (requests.isNotEmpty() && now - requests.first() > timeWindowMillis) {
            requests.removeFirst()
        }

        // Check if we can add a new request
        if (requests.size < maxRequests) {
            requests.addLast(now)
            return true
        }

        return false
    }
}
