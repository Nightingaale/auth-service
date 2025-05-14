package org.nightingaale.authservice.listener;

import lombok.RequiredArgsConstructor;
import org.nightingaale.authservice.entity.*;
import org.nightingaale.authservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthEntityListener {
    private final UserLoginRepository userLoginRepository;
    private final UserLogoutRepository userLogoutRepository;
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRegisteredRepository userRegisteredRepository;
    private final UserRemovedRepository userRemovedRepository;
    private final UserRemoveRepository userRemoveRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AuthEntityListener.class);

    public void saveRegistrationData(UserRegistrationEntity event) {
        try {
            event.setPassword(passwordEncoder.encode(event.getPassword()));
            userRegistrationRepository.save(event);
            logger.info("[REGISTRATION] User registered: userId={}, username={}, email={}",
                    event.getUserId(), event.getUsername(), event.getEmail());
        } catch (Exception e) {
            logger.error("[REGISTRATION] Failed to register user: username={}, error={}",
                    event.getUsername(), e.getMessage());
            throw e;
        }
    }

    public void saveRegisteredData(UserRegisteredEntity event) {
        try {
            userRegisteredRepository.save(event);
            logger.info("[REGISTERED] User registration confirmed: userId={}, time={}",
                    event.getUserId(), event.getRegisteredAt());
        } catch (Exception e) {
            logger.error("[REGISTERED] Failed to confirm registration: userId={}, error={}",
                    event.getUserId(), e.getMessage());
            throw e;
        }
    }

    public void saveLoginData(UserLoginEntity event) {
        try {
            userLoginRepository.save(event);
            logger.info("[LOGIN] User logged in: userId={}, time={}",
                    event.getUsername(), event.getLoginTime());
        } catch (Exception e) {
            logger.error("[LOGIN] Failed to log login event: userId={}, error={}",
                    event.getUsername(), e.getMessage());
            throw e;
        }
    }

    public void saveLogoutData(UserLogoutEntity event) {
        try {
            userLogoutRepository.save(event);
            logger.info("[LOGOUT] User logged out: userId={}, time={}",
                    event.getUserId(), event.getLogoutTime());
        } catch (Exception e) {
            logger.error("[LOGOUT] Failed to log logout event: userId={}, error={}",
                    event.getUserId(), e.getMessage());
            throw e;
        }
    }

    public void saveRemovedData(UserRemovedEntity event) {
        try {
            userRemovedRepository.save(event);
            logger.warn("[REMOVED] User removed: userId={}",
                    event.getUserId());
        } catch (Exception e) {
            logger.error("[REMOVED] Failed to log removal: userId={}, error={}",
                    event.getUserId(), e.getMessage());
            throw e;
        }
    }

    public void saveRemoveData(UserRemoveEntity event) {
        try {
            userRemoveRepository.save(event);
            logger.warn("[REMOVE] User removal requested: userId={}",
                    event.getUserId());
        } catch (Exception e) {
            logger.error("[REMOVE] Failed to log removal request: userId={}, error={}",
                    event.getUserId(), e.getMessage());
            throw e;
        }
    }
}