package javabot.web;

import com.codahale.metrics.health.HealthCheck;

public class JavabotHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
