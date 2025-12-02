package it.univaq.swa.soccorsoweb.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatenteRequest {
    private Long userId;
    private Long patenteId;
    private LocalDate conseguitaIl;
}

