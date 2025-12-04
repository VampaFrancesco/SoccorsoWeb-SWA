package it.univaq.swa.soccorsoweb.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MissioneMezzoResponse {
    private Long missioneId;
    private Long mezzoId;
    private String mezzoNome;
    private String mezzoTipo;
    private String mezzoTarga;
    private LocalDateTime assegnatoAt;
}

