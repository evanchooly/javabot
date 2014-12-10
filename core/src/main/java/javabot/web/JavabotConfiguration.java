package javabot.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.dropwizard.Configuration;
import javabot.web.model.OAuthConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class JavabotConfiguration extends Configuration {
    public static final String SESSION_TOKEN_NAME = "JavabotSession";

    @JsonDeserialize(contentAs = OAuthConfig.class)
    private List<OAuthConfig> oauthCfg;

    @JsonProperty
    private String oAuthSuccessUrl;

    @JsonProperty
    private HashMap<String, String> oauthCustomCfg = null;

    public List<OAuthConfig> getOAuthCfg() {
        return oauthCfg;
    }

    public void setOAuthCfg(final List<OAuthConfig> oauthCfg) {
        this.oauthCfg = oauthCfg;
    }

    public String getOAuthSuccessUrl() {
        return oAuthSuccessUrl;
    }

    public void setOAuthSuccessUrl(final String oAuthSuccessUrl) {
        this.oAuthSuccessUrl = oAuthSuccessUrl;
    }

    public Properties getOAuthCfgProperties() {
        Properties properties = new Properties();
        for (OAuthConfig oauth : oauthCfg) {
            properties.put(oauth.getPrefix() + ".consumer_key",
                           oauth.getKey());
            properties.put(oauth.getPrefix() + ".consumer_secret",
                           oauth.getSecret());
            if (oauth.getPermissions() != null) {
                properties.put(oauth.getPrefix() + ".custom_permissions",
                               oauth.getPermissions());
            }
        }
        if (oauthCustomCfg != null) {
            // add any custom config strings
            properties.putAll(oauthCustomCfg);
        }
        return properties;
    }
}
