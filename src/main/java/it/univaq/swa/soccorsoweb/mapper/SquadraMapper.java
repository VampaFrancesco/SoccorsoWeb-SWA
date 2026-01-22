package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.SquadraRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.SquadraResponse;
import it.univaq.swa.soccorsoweb.model.entity.Squadra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface SquadraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "caposquadra", ignore = true)
    @Mapping(target = "attiva", ignore = true)
    Squadra toEntity(SquadraRequest request);

    @Mapping(target = "caposquadra", source = "caposquadra.utente")
    SquadraResponse toResponse(Squadra entity);

    List<SquadraResponse> toResponseList(List<Squadra> entities);
}
