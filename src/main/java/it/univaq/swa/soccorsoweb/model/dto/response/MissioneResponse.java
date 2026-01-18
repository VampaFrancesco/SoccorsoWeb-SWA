package it.univaq.swa.soccorsoweb.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MissioneResponse {
    private Long id;
    private Long richiestaId;
    private String obiettivo;
    private String posizione;
    private BigDecimal latitudine;
    private BigDecimal longitudine;
    private UserResponse caposquadra;
    private LocalDateTime inizioAt;
    private LocalDateTime fineAt;
    private String commentiFinali;
    private String stato;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<UserResponse> operatori;
    private Integer numeroOperatori;
    private RichiestaSoccorsoResponse richiesta;
}
