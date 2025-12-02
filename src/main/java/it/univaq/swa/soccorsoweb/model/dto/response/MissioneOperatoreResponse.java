package it.univaq.swa.soccorsoweb.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissioneOperatoreResponse {
    private Long missioneId;
    private Long operatoreId;
    private String operatoreNome;
    private String operatoreCognome;
    private String operatoreEmail;
    private LocalDateTime notificatoAt;
    private LocalDateTime assegnatoAt;
}

