package com.badals.shop.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Component
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList("333813192941-5qd2vfdfif0000q1ehu13tkorhtqmqfp.apps.googleusercontent.com"))
            .build();

        String tokenId = (String) authentication.getCredentials();

        // Verify the Google ID token
        GoogleIdToken idToken = null;
        try {
            idToken = googleIdTokenVerifier.verify(tokenId);
        }  catch (Exception e) {
        }
        if (idToken == null) {
            throw new BadCredentialsException("Invalid token id");
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        return new GoogleIdTokenAuthenticationToken(payload, tokenId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
