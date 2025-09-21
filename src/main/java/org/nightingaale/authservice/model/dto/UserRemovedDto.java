package org.nightingaale.authservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRemovedDto {
    private String correlationId;
    private String userId;
    private boolean userExists;
}
