package it.univaq.swa.soccorsoweb.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RichiestaSoccorsoResponse {
    private Long id;
    private String descrizione;
    private String indirizzo;
    private BigDecimal latitudine;
    private BigDecimal longitudine;
    private String nomeSegnalante;
    private String emailSegnalante;
    private String telefonoSegnalante;
    private String fotoUrl;
    private String stato;
    private LocalDateTime convalidataAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long missioneId;
    private String ipOrigine;
    private String tokenConvalida;
}
