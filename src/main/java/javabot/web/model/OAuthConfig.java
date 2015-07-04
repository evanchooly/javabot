package javabot.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthConfig {
    @JsonProperty
    private String url;
    @JsonProperty
    private String name;
    @JsonProperty
    private String prefix;
    @JsonProperty
    private String key;
    @JsonProperty
    private String secret;
    @JsonProperty
    private String permissions;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public String getPermissions() {
        return permissions;
    }
}
