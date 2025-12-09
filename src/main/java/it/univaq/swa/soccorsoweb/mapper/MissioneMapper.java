package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.MissioneRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.Missione;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, MissioneOperatoreMapper.class}
)
public interface MissioneMapper {

    // ========== Request → Entity ==========
    // I campi richiestaId, caposquadraId, operatoriIds
    // del Request vengono gestiti dal service layer e non mappati direttamente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "richiesta", ignore = true)
    @Mapping(target = "caposquadra", ignore = true)
    @Mapping(target = "missioneOperatori", ignore = true)
    @Mapping(target = "stato", constant = "IN_CORSO")
    @Mapping(target = "inizioAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "fineAt", ignore = true)
    @Mapping(target = "livelloSuccesso", ignore = true)
    @Mapping(target = "commentiFinali", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Missione toEntity(MissioneRequest request);

    // ========== Entity → Response ==========
    @Mapping(target = "richiestaId", source = "richiesta.id")
    @Mapping(target = "richiesta", source = "richiesta")
    @Mapping(target = "numeroOperatori", expression = "java(entity.getMissioneOperatori() != null ? entity.getMissioneOperatori().size() : 0)")
    @Mapping(target = "operatori", expression = "java(mapMissioneOperatoriToUsers(entity))")
    MissioneResponse toResponse(Missione entity);

    // ========== List mapping ==========
    List<MissioneResponse> toResponseList(List<Missione> entities);

    // ========== Helper methods ==========
    @Named("mapMissioneOperatoriToUsers")
    default Set<UserResponse> mapMissioneOperatoriToUsers(Missione missione) {
        if (missione == null || missione.getMissioneOperatori() == null) {
            return new java.util.HashSet<>();
        }

        // MapStruct inietta automaticamente userMapper quando usa questo mapper
        return missione.getMissioneOperatori().stream()
                .filter(mo -> mo.getOperatore() != null)
                .map(mo -> {
                    UserResponse response = new UserResponse();
                    response.setId(mo.getOperatore().getId());
                    response.setEmail(mo.getOperatore().getEmail());
                    response.setNome(mo.getOperatore().getNome());
                    response.setCognome(mo.getOperatore().getCognome());
                    response.setDataNascita(mo.getOperatore().getDataNascita());
                    response.setTelefono(mo.getOperatore().getTelefono());
                    response.setIndirizzo(mo.getOperatore().getIndirizzo());
                    response.setAttivo(mo.getOperatore().getAttivo());
                    response.setDisponibile(mo.getOperatore().getDisponibile());
                    response.setCreatedAt(mo.getOperatore().getCreatedAt());
                    response.setUpdatedAt(mo.getOperatore().getUpdatedAt());
                    // Non mappiamo i roles per evitare il problema con roleMapper
                    response.setRoles(new java.util.HashSet<>());
                    return response;
                })
                .collect(Collectors.toSet());
    }
}

