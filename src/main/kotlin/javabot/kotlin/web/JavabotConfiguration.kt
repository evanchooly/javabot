package javabot.kotlin.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.dropwizard.Configuration
import javabot.kotlin.web.model.OAuthConfig

import java.util.HashMap
import java.util.Properties

class JavabotConfiguration : Configuration() {

    companion object {
        val SESSION_TOKEN_NAME: String = "JavabotSession"
    }

    @JsonDeserialize(contentAs = OAuthConfig::class)
    var OAuthCfg: List<OAuthConfig>? = null

    @JsonProperty
    var OAuthSuccessUrl: String? = null

    @JsonProperty
    private val oauthCustomCfg: HashMap<String, String>? = null

    fun getOAuthCfgProperties(): Properties {
        val properties = Properties()
        for (oauth in OAuthCfg!!) {
            properties.put(oauth.prefix + ".consumer_key",
                  oauth.key)
            properties.put(oauth.prefix + ".consumer_secret",
                  oauth.secret)
            if (oauth.permissions != null) {
                properties.put(oauth.prefix + ".custom_permissions",
                      oauth.permissions)
            }
        }
        if (oauthCustomCfg != null) {
            // add any custom config strings
            properties.putAll(oauthCustomCfg)
        }
        return properties
    }
}
