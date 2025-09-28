package org.nightingaale.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.nightingaale.authservice.event.KafkaUserUpdateRequestEvent;
import org.nightingaale.authservice.model.dto.*;
import org.nightingaale.authservice.listener.AuthServiceListener;
import org.nightingaale.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthServiceController {

    private final AuthServiceListener authDtoListener;
    private final BCryptPasswordEncoder passwordEncoder;;
    private final AuthService authService;


    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signUp(@RequestBody UserRegistrationDto event) {
        event.setCorrelationId(UUID.randomUUID().toString());
        event.setPassword(passwordEncoder.encode(event.getPassword()));
        authDtoListener.saveRegistrationEvent(event);
        return ResponseEntity.ok("[User has successfully been signed up!]");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserLoginDto event) {
        event.setCorrelationId(UUID.randomUUID().toString());
        authDtoListener.saveLoginEvent(event);
        return ResponseEntity.ok("[User has successfully been signed in!]");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Jwt jwt) {
        authService.logoutUser(jwt.getSubject());
        return ResponseEntity.ok("[User has successfully been logged out!]");
    }

    @PostMapping("/remove-user")
    public ResponseEntity<?> removeUser(@RequestBody UserRemoveDto event) {
        event.setCorrelationId(UUID.randomUUID().toString());
        authDtoListener.saveRemoveEvent(event);
        return ResponseEntity.ok("[User with ID: " + event.getUserId() + "has successfully been removed!]");
    }

    @PatchMapping("/updated-user")
    public ResponseEntity<?> updatedUser(@RequestBody KafkaUserUpdateRequestEvent event, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        event.setUserId(userId.toString());
        authDtoListener.updatedUserEvent(event);
        return ResponseEntity.ok("[User has successfully been updated!]");
    }
}
