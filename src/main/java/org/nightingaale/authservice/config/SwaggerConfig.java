package org.nightingaale.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "auth-service",
                version = "v1",
                description = "Responsible for logic of log- and reg-"
        )
)
public class SwaggerConfig {
}
