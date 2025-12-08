package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RichiestaSoccorsoMapper {

    // ========== Request → Entity ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tokenConvalida", ignore = true)
    @Mapping(target = "stato", constant = "INVIATA")
    @Mapping(target = "ipOrigine", ignore = true)
    @Mapping(target = "fotoUrl", ignore = true)
    @Mapping(target = "convalidataAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "missione", ignore = true)
    RichiestaSoccorso toEntity(RichiestaSoccorsoRequest request);

    // ========== Entity → Response ==========
    @Mapping(target = "missioneId", source = "missione.id")
    RichiestaSoccorsoResponse toResponse(RichiestaSoccorso entity);

    // ========== List mapping ==========
    List<RichiestaSoccorsoResponse> toResponseList(List<RichiestaSoccorso> entities);
}
