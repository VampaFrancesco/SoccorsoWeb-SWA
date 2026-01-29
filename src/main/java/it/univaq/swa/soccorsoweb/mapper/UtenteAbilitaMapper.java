package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.UtenteAbilitaResponse;
import it.univaq.swa.soccorsoweb.model.entity.UtenteAbilita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { AbilitaMapper.class })
public interface UtenteAbilitaMapper {

    // ========== Entity → Response ==========
    @Mapping(target = "userId", source = "utente.id")
    @Mapping(target = "abilitaId", source = "abilita.id")
    @Mapping(target = "abilita", source = "abilita")
    UtenteAbilitaResponse toResponse(UtenteAbilita entity);

    // ========== List mapping ==========
    List<UtenteAbilitaResponse> toResponseList(List<UtenteAbilita> entities);
}
