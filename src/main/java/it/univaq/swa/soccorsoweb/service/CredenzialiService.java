package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.model.dto.response.CredenzialiResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.Role;
import it.univaq.swa.soccorsoweb.model.entity.User;
import it.univaq.swa.soccorsoweb.repository.RoleRepository;
import it.univaq.swa.soccorsoweb.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;

@Service
@Data
public class CredenzialiService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    Random random = new Random();
    String dominio = "soccorsoweb.it";


    public void generaCredenziali(String type) {
        User u = new User();
        u.setRoles(new HashSet<>());

        if(type.equalsIgnoreCase("admin")) {
            Role adminRole = roleRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Ruolo admin non trovato"));
            u.getRoles().add(adminRole);
        }else {
            Role userRole = roleRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Ruolo user non trovato"));
            u.getRoles().add(userRole);
        }
        String username = type.toLowerCase() + random.nextInt(1000, 9999) + "@" + dominio;
        String password = "Passw0rd!" + random.nextInt(1000, 9999);
        u.setEmail(username);
        u.setPassword(password);
        userRepository.save(u);
        emailService.inviaCredenziali("francesco.vampa@student.univaq.it", username, password);
    }
}
