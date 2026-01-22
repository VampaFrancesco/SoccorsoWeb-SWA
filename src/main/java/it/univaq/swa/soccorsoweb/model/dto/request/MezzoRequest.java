package it.univaq.swa.soccorsoweb.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MezzoRequest {
    @NotBlank
    private String nome;
    private String descrizione;
    private String tipo;
    private String targa;
    @Builder.Default
    private Boolean disponibile = true;
}
