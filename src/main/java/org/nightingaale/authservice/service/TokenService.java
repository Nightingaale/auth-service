package org.nightingaale.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final RedisTemplate<String, AccessTokenResponse> redisTemplate;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.credentials.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.credentials.secret}")
    private String keycloakClientSecret;

    public AccessTokenResponse getCachedToken(String userId, String username, String password) {
        String key = "AUTH_TOKEN: " + userId;
        log.info("[Authenticating user: {}", username);
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .username(username)
                .password(password)
                .build();

        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();
        redisTemplate.opsForValue().set(key, accessToken, Duration.ofSeconds(accessToken.getRefreshExpiresIn()));

        log.info("[Generated token for user: {}, with userId {}]", username, userId);
        return accessToken;
    }
}