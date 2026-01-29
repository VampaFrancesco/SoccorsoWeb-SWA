package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.SquadraOperatoreResponse;
import it.univaq.swa.soccorsoweb.model.entity.SquadraOperatore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface SquadraOperatoreMapper {

    // ========== Entity → Response ==========
    @Mapping(target = "squadraId", source = "squadra.id")
    @Mapping(target = "userId", source = "operatore.id")
    @Mapping(target = "operatore", source = "operatore")
    SquadraOperatoreResponse toResponse(SquadraOperatore entity);

    // ========== List mapping ==========
    List<SquadraOperatoreResponse> toResponseList(List<SquadraOperatore> entities);
}
