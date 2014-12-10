package javabot.web.model;

import com.google.common.collect.Sets;
import org.brickred.socialauth.util.AccessGrant;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private Set<Authority> authorities = new HashSet<>();

    private String email;

    private UUID sessionToken;
    private String openIDIdentifier;
    private AccessGrant OAuthInfo;

    public User(final UUID uuid) {
        sessionToken = uuid;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(final Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public UUID getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(final UUID sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getOpenIDIdentifier() {
        return openIDIdentifier;
    }

    public void setOpenIDIdentifier(final String openIDIdentifier) {
        this.openIDIdentifier = openIDIdentifier;
    }

    public boolean hasAllAuthorities(Set<Authority> requiredAuthorities) {
        return authorities.containsAll(requiredAuthorities);
    }

    public boolean hasAuthority(Authority authority) {
        return hasAllAuthorities(Sets.newHashSet(authority));
    }

    public AccessGrant getOAuthInfo() {
        return OAuthInfo;
    }

    public void setOAuthInfo(final AccessGrant OAuthInfo) {
        this.OAuthInfo = OAuthInfo;
    }
}