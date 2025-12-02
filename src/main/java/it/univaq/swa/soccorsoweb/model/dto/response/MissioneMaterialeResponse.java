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
public class MissioneMaterialeResponse {
    private Long missioneId;
    private Long materialeId;
    private String materialeNome;
    private String materialeTipo;
    private Integer quantitaUsata;
    private LocalDateTime assegnatoAt;
}

