package it.univaq.swa.soccorsoweb.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatenteResponse {
    private Long userId;
    private Long patenteId;
    private String patenteTipo;
    private String patenteDescrizione;
    private LocalDate conseguitaIl;
}

