package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.MissioneMezzoResponse;
import it.univaq.swa.soccorsoweb.model.entity.MissioneMezzo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MissioneMezzoMapper {

    @Mapping(target = "missioneId", source = "missione.id")
    @Mapping(target = "mezzoId", source = "mezzo.id")
    @Mapping(target = "mezzoNome", source = "mezzo.nome")
    @Mapping(target = "mezzoTipo", source = "mezzo.tipo")
    @Mapping(target = "mezzoTarga", source = "mezzo.targa")
    MissioneMezzoResponse toResponse(MissioneMezzo entity);

    List<MissioneMezzoResponse> toResponseList(List<MissioneMezzo> entities);
}

