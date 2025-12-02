package it.univaq.swa.soccorsoweb.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissioneOperatoreRequest {
    private Long missioneId;
    private Long operatoreId;
    private LocalDateTime notificatoAt;
    private LocalDateTime assegnatoAt;
}

