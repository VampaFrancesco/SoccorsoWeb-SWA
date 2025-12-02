package it.univaq.swa.soccorsoweb.mapper;

import it.univaq.swa.soccorsoweb.model.dto.response.UserPatenteResponse;
import it.univaq.swa.soccorsoweb.model.entity.UserPatente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserPatenteMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "patenteId", source = "patente.id")
    @Mapping(target = "patenteTipo", source = "patente.tipo")
    @Mapping(target = "patenteDescrizione", source = "patente.descrizione")
    UserPatenteResponse toResponse(UserPatente entity);

    List<UserPatenteResponse> toResponseList(List<UserPatente> entities);
}

