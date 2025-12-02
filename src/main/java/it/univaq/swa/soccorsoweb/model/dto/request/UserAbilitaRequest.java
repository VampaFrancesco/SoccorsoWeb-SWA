package it.univaq.swa.soccorsoweb.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAbilitaRequest {
    private Long userId;
    private Long abilitaId;
    private String livello;
}

