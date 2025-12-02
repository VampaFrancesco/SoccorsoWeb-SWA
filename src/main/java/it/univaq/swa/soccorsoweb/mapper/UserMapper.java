package it.univaq.swa.soccorsoweb.mapper;


import it.univaq.swa.soccorsoweb.model.dto.request.UserUpdateRequest;
import it.univaq.swa.soccorsoweb.model.dto.request.UserRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.AbilitaResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.PatenteResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class, PatenteMapper.class, AbilitaMapper.class, UserPatenteMapper.class, UserAbilitaMapper.class}
)
public interface UserMapper {

    // ========== Request → Entity (Creazione) ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attivo", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "userPatenti", ignore = true)
    @Mapping(target = "userAbilita", ignore = true)
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
    @Mapping(target = "userPatenti", ignore = true)
    @Mapping(target = "userAbilita", ignore = true)
    @Mapping(target = "missioniComeOperatore", ignore = true)
    @Mapping(target = "missioniComeCaposquadra", ignore = true)
    void updateEntityFromDto(UserUpdateRequest request, @MappingTarget User user);

    // ========== Entity → Response ==========
    @Mapping(target = "patenti", expression = "java(mapUserPatentiToPatenti(user))")
    @Mapping(target = "abilita", expression = "java(mapUserAbilitaToAbilita(user))")
    UserResponse toResponse(User user);

    // ========== List mapping ==========
    List<UserResponse> toResponseList(List<User> users);

    // ========== Helper methods ==========
    default Set<PatenteResponse> mapUserPatentiToPatenti(User user) {
        if (user.getUserPatenti() == null) return new java.util.HashSet<>();
        return user.getUserPatenti().stream()
                .map(up -> PatenteResponse.builder()
                        .id(up.getPatente().getId())
                        .tipo(up.getPatente().getTipo())
                        .descrizione(up.getPatente().getDescrizione())
                        .build())
                .collect(java.util.stream.Collectors.toSet());
    }

    default Set<AbilitaResponse> mapUserAbilitaToAbilita(User user) {
        if (user.getUserAbilita() == null) return new java.util.HashSet<>();
        return user.getUserAbilita().stream()
                .map(ua -> AbilitaResponse.builder()
                        .id(ua.getAbilita().getId())
                        .nome(ua.getAbilita().getNome())
                        .descrizione(ua.getAbilita().getDescrizione())
                        .build())
                .collect(java.util.stream.Collectors.toSet());
    }
}
