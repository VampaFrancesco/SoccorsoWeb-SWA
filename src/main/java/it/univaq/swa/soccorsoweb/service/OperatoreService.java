package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.UserMapper;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.User;
import it.univaq.swa.soccorsoweb.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OperatoreService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public OperatoreService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<UserResponse> operatoreDisponibile(boolean disponibili) {
        List<User> operatori = userRepository.findOperatoriByDisponibile(disponibili);
        return userMapper.toResponseList(operatori);
    }

    public UserResponse dettagliOperatore(Long id){

        User operatore = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Operatore non trovato con ID: " + id));
        if(operatore.getRoles().stream().noneMatch(role -> role.getName().equals("OPERATORE"))) {
            log.info("Gli operatori hanno matchato");
            return null;}
        return userMapper.toResponse(operatore);
    }
}
