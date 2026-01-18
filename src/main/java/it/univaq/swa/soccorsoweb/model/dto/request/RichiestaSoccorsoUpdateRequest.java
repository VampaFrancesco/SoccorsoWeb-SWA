package it.univaq.swa.soccorsoweb.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO per la modifica parziale di una richiesta di soccorso
 * Tutti i campi sono opzionali per permettere modifiche parziali
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RichiestaSoccorsoUpdateRequest {

    private String descrizione;

    private String indirizzo;

    @DecimalMin(value = "-90.0", message = "La latitudine deve essere >= -90")
    @DecimalMax(value = "90.0", message = "La latitudine deve essere <= 90")
    private BigDecimal latitudine;

    @DecimalMin(value = "-180.0", message = "La longitudine deve essere >= -180")
    @DecimalMax(value = "180.0", message = "La longitudine deve essere <= 180")
    private BigDecimal longitudine;

    private String nomeSegnalante;

    @Email(message = "L'email del segnalante deve essere valida")
    private String emailSegnalante;

    private String fotoUrl;

    private String telefonoSegnalante;

    private String stato; // INVIATA, CONVALIDATA, IN_CORSO, CHIUSA, ANNULLATA, IGNORATA

    @Min(value = 1, message = "Il livello di successo deve essere compreso tra 1 e 10")
    @Max(value = 10, message = "Il livello di successo deve essere compreso tra 1 e 10")
    private Integer livelloSuccesso;
}
