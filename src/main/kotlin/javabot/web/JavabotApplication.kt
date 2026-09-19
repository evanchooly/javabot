package javabot.web

import com.google.inject.Injector
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import javabot.Javabot
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory

@ApplicationScoped
class JavabotApplication @Inject constructor(var injector: Injector) {
    var running = false

    @ConfigProperty(name = "javabot.web.enabled", defaultValue = "false")
    var webEnabled: Boolean = false

    companion object {
        private val LOG = LoggerFactory.getLogger(JavabotApplication::class.java)
    }

    fun onStart(@Observes ev: StartupEvent) {
        if (webEnabled) {
            LOG.info("Starting Javabot web application")
            injector.getInstance(Javabot::class.java).start()
            running = true
        } else {
            LOG.info("Javabot web application is disabled")
        }
    }
}
// JavadocFilter (a ContainerRequestFilter that served files out of the on-disk `javadoc/`
// directory) used to live here. It was removed: no resource in this app maps /javadoc/*, and a
// non-@PreMatching ContainerRequestFilter only runs once a route has already matched, so it never
// executed. The on-disk javadoc/ directory is not under META-INF/resources either, so Quarkus's
// static-resource serving does not reach it. Meanwhile the filter built its file path by
// concatenating unsanitised, URL-decoded request segments, which would have been a path-traversal
// hole the moment a /javadoc/* route did appear. Serving javadoc should be reintroduced (if
// wanted) as a real resource that resolves and validates paths against the javadoc root.
