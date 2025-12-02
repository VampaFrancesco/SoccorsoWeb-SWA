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
public class MissioneMezzoResponse {
    private Long missioneId;
    private Long mezzoId;
    private String mezzoNome;
    private String mezzoTipo;
    private String mezzoTarga;
    private LocalDateTime assegnatoAt;
}

