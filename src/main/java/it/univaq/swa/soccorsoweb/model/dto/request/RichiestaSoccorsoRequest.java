package it.univaq.swa.soccorsoweb.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RichiestaSoccorsoRequest {

    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    private String indirizzo;

    @NotNull(message = "La latitudine è obbligatoria")
    private BigDecimal latitudine;

    @NotNull(message = "La longitudine è obbligatoria")
    private BigDecimal longitudine;

    @NotBlank(message = "Il nome del segnalante è obbligatorio")
    private String nomeSegnalante;

    @NotBlank(message = "L'email del segnalante è obbligatoria")
    @Email(message = "L'email del segnalante deve essere valida")
    private String emailSegnalante;

    private String telefonoSegnalante;
}
