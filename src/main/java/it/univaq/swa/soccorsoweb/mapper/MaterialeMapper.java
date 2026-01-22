package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.MaterialeRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MaterialeResponse;
import it.univaq.swa.soccorsoweb.model.entity.Materiale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaterialeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "disponibile", ignore = true)
    Materiale toEntity(MaterialeRequest request);

    MaterialeResponse toResponse(Materiale entity);

    List<MaterialeResponse> toResponseList(List<Materiale> entities);
}
