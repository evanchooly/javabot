package javabot.kotlin.web

import com.codahale.metrics.health.HealthCheck

public class JavabotHealthCheck : HealthCheck() {
    @Throws(Exception::class)
    override fun check(): HealthCheck.Result {
        return HealthCheck.Result.healthy()
    }
}
