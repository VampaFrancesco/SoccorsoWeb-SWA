package it.univaq.swa.soccorsoweb.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAbilitaResponse {
    private Long userId;
    private Long abilitaId;
    private String abilitaNome;
    private String abilitaDescrizione;
    private String livello;
}

