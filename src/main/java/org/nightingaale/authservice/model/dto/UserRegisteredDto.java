package org.nightingaale.authservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredDto {
    private String correlationId;
    private String userId;
    private boolean userExists;
}
