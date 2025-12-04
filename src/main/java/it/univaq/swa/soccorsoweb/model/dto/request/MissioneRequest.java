package it.univaq.swa.soccorsoweb.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MissioneRequest {

    @NotBlank(message = "L'obiettivo è obbligatorio")
    private String obiettivo;

    private String posizione;

    private BigDecimal latitudine;

    private BigDecimal longitudine;

    // IDs per le relazioni che verranno gestite dal service
    private Long richiestaId;
    private Long caposquadraId;
    private Set<Long> operatoriIds;
    private Set<Long> mezziIds;
    private Set<Long> materialiIds;
}
