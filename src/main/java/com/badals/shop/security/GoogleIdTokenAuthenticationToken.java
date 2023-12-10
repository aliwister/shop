package com.badals.shop.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GoogleIdTokenAuthenticationToken extends AbstractAuthenticationToken {

    private String IdToken;
    private GoogleIdToken.Payload payload;

    public GoogleIdTokenAuthenticationToken(String IdToken) {
        super((Collection)null);
        this.IdToken = IdToken;
        this.setAuthenticated(false);
    }

    public GoogleIdTokenAuthenticationToken(GoogleIdToken.Payload payload, String IdToken) {
        super((Collection)null);
        this.payload = payload;
        this.IdToken = IdToken;
        super.setAuthenticated(true);
    }

    public GoogleIdTokenAuthenticationToken(GoogleIdToken.Payload payload, String IdToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.payload = payload;
        this.IdToken = IdToken;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.IdToken;
    }

    @Override
    public Object getPrincipal() {
        return this.payload;
    }

}
