package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.MissioneMaterialeResponse;
import it.univaq.swa.soccorsoweb.model.entity.MissioneMateriale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MissioneMaterialeMapper {

    @Mapping(target = "missioneId", source = "missione.id")
    @Mapping(target = "materialeId", source = "materiale.id")
    @Mapping(target = "materialeNome", source = "materiale.nome")
    @Mapping(target = "materialeTipo", source = "materiale.tipo")
    MissioneMaterialeResponse toResponse(MissioneMateriale entity);

    List<MissioneMaterialeResponse> toResponseList(List<MissioneMateriale> entities);
}

