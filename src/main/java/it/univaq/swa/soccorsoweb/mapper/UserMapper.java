package it.univaq.swa.soccorsoweb.mapper;


import it.univaq.swa.soccorsoweb.model.dto.request.UserUpdateRequest;
import it.univaq.swa.soccorsoweb.model.dto.request.UserRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class}
)
public interface UserMapper {

    // ========== Request → Entity (Creazione) ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attivo", constant = "true")
    @Mapping(target = "disponibile", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "missioniComeOperatore", ignore = true)
    @Mapping(target = "missioniComeCaposquadra", ignore = true)
    User toEntity(UserRequest request);

    // ========== UpdateRequest → Entity (Aggiornamento) ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "attivo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "missioniComeOperatore", ignore = true)
    @Mapping(target = "missioniComeCaposquadra", ignore = true)
    void updateEntityFromDto(UserUpdateRequest request, @MappingTarget User user);

    // ========== Entity → Response ==========
    @Mapping(target = "token", ignore = true)
    UserResponse toResponse(User user);

    // ========== List mapping ==========
    List<UserResponse> toResponseList(List<User> users);
}
