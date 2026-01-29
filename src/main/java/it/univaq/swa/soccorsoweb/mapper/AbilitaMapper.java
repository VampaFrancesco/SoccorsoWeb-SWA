package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.AbilitaRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.AbilitaResponse;
import it.univaq.swa.soccorsoweb.model.entity.Abilita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AbilitaMapper {

    // ========== Request → Entity ==========
    @Mapping(target = "id", ignore = true)
    Abilita toEntity(AbilitaRequest request);

    // ========== Entity → Response ==========
    AbilitaResponse toResponse(Abilita entity);

    // ========== List mapping ==========
    List<AbilitaResponse> toResponseList(List<Abilita> entities);
}
