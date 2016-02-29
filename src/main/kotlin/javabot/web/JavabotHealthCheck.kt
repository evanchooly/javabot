package javabot.web

import com.codahale.metrics.health.HealthCheck

class JavabotHealthCheck : HealthCheck() {
    @Throws(Exception::class)
    override fun check(): Result {
        return Result.healthy()
    }
}
