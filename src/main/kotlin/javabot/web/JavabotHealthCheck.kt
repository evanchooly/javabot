package javabot.web

import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness
import javax.enterprise.context.ApplicationScoped

@Liveness
@ApplicationScoped
class JavabotHealthCheck : HealthCheck {
    override fun call(): HealthCheckResponse {
        return HealthCheckResponse.up("javabot")
    }
}
