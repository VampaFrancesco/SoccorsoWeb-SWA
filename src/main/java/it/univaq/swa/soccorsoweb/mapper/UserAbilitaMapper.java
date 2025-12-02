package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.UserAbilitaResponse;
import it.univaq.swa.soccorsoweb.model.entity.UserAbilita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAbilitaMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "abilitaId", source = "abilita.id")
    @Mapping(target = "abilitaNome", source = "abilita.nome")
    @Mapping(target = "abilitaDescrizione", source = "abilita.descrizione")
    UserAbilitaResponse toResponse(UserAbilita entity);

    List<UserAbilitaResponse> toResponseList(List<UserAbilita> entities);
}

