package org.nightingaale.authservice.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.nightingaale.authservice.event.KafkaUserUpdateRequestEvent;
import org.nightingaale.authservice.repository.UserLoginRepository;
import org.nightingaale.authservice.repository.UserRegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.credentials.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.credentials.secret}")
    private String keycloakClientSecret;

    private final TokenService tokenService;
    private final UserLoginRepository userLoginRepository;
    private final UserRegistrationRepository userRegistrationRepository;

    private Keycloak getAdminKeycloakInstance() {
        log.info("[Creating Keycloak administration instance]");
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .build();
    }

    public String registerUser(String username, String email, String password) {
        log.info("[Attempting to register user: {}]", username);
        Keycloak keycloak = getAdminKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setFirstName(username);
        user.setLastName(username);

        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            if (response.getStatus() == 409) {
                log.warn("[User with this username or email already exists: {}, {}]", username, email);
                throw new RuntimeException("[User with this username or email already exists in Keycloak]");
            }
            log.error("[Failed to create user in Keycloak: {}]", response.getStatusInfo());
            throw new RuntimeException("[Failed to create user in Keycloak: " + response.getStatusInfo() + "]");
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        keycloak.realm(keycloakRealm).users().get(userId).resetPassword(credential);

        RoleRepresentation userRole = realmResource.roles().get("user").toRepresentation();
        keycloak.realm(keycloakRealm).users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));
        log.info("[User successfully registered in Keycloak with ID: {}]", userId);
        return userId;
    }

    public AccessTokenResponse authenticateUser(String username, String password) {
        String userId = getAdminKeycloakInstance()
                .realm(keycloakRealm)
                .users()
                .search(username)
                .stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("[User has not found]"))
                .getId();
        return tokenService.getCachedToken(userId, username, password);
    }

    @CacheEvict(value = "AUTH_TOKEN", key = "#userId")
    public void logoutUser(String userId) {
        log.info("[Logging out user with ID: {}]", userId);
        Keycloak keycloak = getAdminKeycloakInstance();
        try {
            UserResource userResource = keycloak.realm(keycloakRealm).users().get(userId);
            userResource.logout();
            log.info("[User with ID: {}", userId + "logged out successfully]");
        } catch (Exception e) {
            log.warn("[Error while logging out user with ID: {}. Error: {}]", userId, e.getMessage(), e);
        }
    }

    @CacheEvict(value = "AUTH_TOKEN", key = "#userId")
    public void removeUser(String userId) {
        log.info("[Attempting to remove user with ID: {}]", userId);

        Keycloak keycloak = getAdminKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        try {
            if (usersResource.get(userId).toRepresentation() != null) {
                usersResource.delete(userId);
                log.info("[User with userID: {} has been deleted from Keycloak]", userId);
            } else {
                log.warn("[User with userID: {} has not been deleted from Keycloak]", userId);
                throw new RuntimeException("[User has not found in Keycloak]");
            }
        } catch (Exception e) {
            log.error("[Failed to remove user with userID: {}. Error: {}]", userId, e.getMessage(), e);
        }
    }

    public void updateUser(KafkaUserUpdateRequestEvent userUpdateRequestEvent) {
        log.info("[Attempting to update user with ID: {}]", userUpdateRequestEvent.getUserId());
        try {
            String userId = userUpdateRequestEvent.getUserId();

            Keycloak keycloak = getAdminKeycloakInstance();
            RealmResource realmResource = keycloak.realm(keycloakRealm);
            UserResource userResource = realmResource.users().get(userId);

            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(userUpdateRequestEvent.getUsername());

            userRepresentation.setFirstName(userUpdateRequestEvent.getUsername());
            userRepresentation.setLastName(userUpdateRequestEvent.getUsername());

            userRepresentation.setEmail(userUpdateRequestEvent.getEmail());
            userRepresentation.setEnabled(true);
            userResource.update(userRepresentation);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userUpdateRequestEvent.getPassword());
            userResource.resetPassword(credential);

        } catch (RuntimeException e) {
            log.error("[Failed to update user with ID in Keycloak: {}. Error: {}]", userUpdateRequestEvent.getUserId(), e.getMessage());
            throw e;
        }
    }
}