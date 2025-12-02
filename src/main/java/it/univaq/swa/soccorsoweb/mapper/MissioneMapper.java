package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.MissioneRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MaterialeResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.MezzoResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.Missione;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, MezzoMapper.class, MaterialeMapper.class,
                MissioneOperatoreMapper.class, MissioneMezzoMapper.class, MissioneMaterialeMapper.class}
)
public interface MissioneMapper {

    // ========== Request → Entity ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "richiesta", ignore = true)
    @Mapping(target = "caposquadra", ignore = true)
    @Mapping(target = "missioneOperatori", ignore = true)
    @Mapping(target = "missioneMezzi", ignore = true)
    @Mapping(target = "missioneMateriali", ignore = true)
    @Mapping(target = "stato", constant = "IN_CORSO")
    @Mapping(target = "inizioAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "fineAt", ignore = true)
    @Mapping(target = "livelloSuccesso", ignore = true)
    @Mapping(target = "commentiFinali", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "aggiornamenti", ignore = true)
    Missione toEntity(MissioneRequest request);

    // ========== Entity → Response ==========
    @Mapping(target = "richiestaId", source = "richiesta.id")
    @Mapping(target = "numeroOperatori", expression = "java(entity.getMissioneOperatori().size())")
    @Mapping(target = "operatori", expression = "java(mapMissioneOperatoriToUsers(entity))")
    @Mapping(target = "mezzi", expression = "java(mapMissioneMezziToMezzi(entity))")
    @Mapping(target = "materiali", expression = "java(mapMissioneMaterialiToMateriali(entity))")
    MissioneResponse toResponse(Missione entity);

    // ========== List mapping ==========
    List<MissioneResponse> toResponseList(List<Missione> entities);

    // ========== Helper methods ==========
    default Set<UserResponse> mapMissioneOperatoriToUsers(Missione missione) {
        if (missione.getMissioneOperatori() == null) return new java.util.HashSet<>();
        UserMapper userMapper = new it.univaq.swa.soccorsoweb.mapper.UserMapperImpl();
        return missione.getMissioneOperatori().stream()
                .map(mo -> userMapper.toResponse(mo.getOperatore()))
                .collect(java.util.stream.Collectors.toSet());
    }

    default Set<MezzoResponse> mapMissioneMezziToMezzi(Missione missione) {
        if (missione.getMissioneMezzi() == null) return new java.util.HashSet<>();
        MezzoMapper mezzoMapper = new it.univaq.swa.soccorsoweb.mapper.MezzoMapperImpl();
        return missione.getMissioneMezzi().stream()
                .map(mm -> mezzoMapper.toResponse(mm.getMezzo()))
                .collect(java.util.stream.Collectors.toSet());
    }

    default Set<MaterialeResponse> mapMissioneMaterialiToMateriali(Missione missione) {
        if (missione.getMissioneMateriali() == null) return new java.util.HashSet<>();
        MaterialeMapper materialeMapper = new it.univaq.swa.soccorsoweb.mapper.MaterialeMapperImpl();
        return missione.getMissioneMateriali().stream()
                .map(mm -> materialeMapper.toResponse(mm.getMateriale()))
                .collect(java.util.stream.Collectors.toSet());
    }
}
