package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.MissioneOperatoreResponse;
import it.univaq.swa.soccorsoweb.model.entity.MissioneOperatore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MissioneOperatoreMapper {

    @Mapping(target = "missioneId", source = "missione.id")
    @Mapping(target = "operatoreId", source = "operatore.id")
    @Mapping(target = "operatoreNome", source = "operatore.nome")
    @Mapping(target = "operatoreCognome", source = "operatore.cognome")
    @Mapping(target = "operatoreEmail", source = "operatore.email")
    MissioneOperatoreResponse toResponse(MissioneOperatore entity);

    List<MissioneOperatoreResponse> toResponseList(List<MissioneOperatore> entities);
}

