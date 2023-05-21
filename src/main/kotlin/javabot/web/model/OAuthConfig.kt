package javabot.web.model

import com.fasterxml.jackson.annotation.JsonProperty

class OAuthConfig {
    @JsonProperty val url: String? = null
    @JsonProperty val name: String? = null
    @JsonProperty val prefix: String? = null
    @JsonProperty val key: String? = null
    @JsonProperty val secret: String? = null
    @JsonProperty val permissions: String? = null
}
