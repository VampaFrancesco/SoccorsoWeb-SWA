package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.request.MezzoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MezzoResponse;
import it.univaq.swa.soccorsoweb.model.entity.Mezzo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MezzoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Mezzo toEntity(MezzoRequest request);

    MezzoResponse toResponse(Mezzo entity);

    List<MezzoResponse> toResponseList(List<Mezzo> entities);
}
