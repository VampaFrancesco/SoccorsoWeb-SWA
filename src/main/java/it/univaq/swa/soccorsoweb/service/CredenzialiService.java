package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.UserMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.NuovoOperatoreRequest;
import it.univaq.swa.soccorsoweb.model.dto.request.UserRequest;
import it.univaq.swa.soccorsoweb.model.entity.Role;
import it.univaq.swa.soccorsoweb.model.entity.User;
import it.univaq.swa.soccorsoweb.repository.RoleRepository;
import it.univaq.swa.soccorsoweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredenzialiService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    Random random = new Random();
    String dominio = "soccorsoweb.it";

    public void generaCredenziali(String type) {
        User u = new User();
        u.setRoles(new HashSet<>());

        if (type.equalsIgnoreCase("admin")) {
            // ID 1 = ADMIN according to DB population
            Role adminRole = roleRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Ruolo admin non trovato"));
            u.getRoles().add(adminRole);
        } else {
            // ID 2 = OPERATORE according to DB population
            Role userRole = roleRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Ruolo user non trovato"));
            u.getRoles().add(userRole);
        }
        String username = type.toLowerCase() + random.nextInt(1000, 9999) + "@" + dominio;
        String password = "Passw0rd!" + random.nextInt(1000, 9999);

        u.setEmail(username);
        u.setNome(type);
        u.setCognome(type);
        u.setAttivo(true);
        u.setPassword(passwordEncoder.encode(password)); // Hash it!

        userRepository.save(u);
        emailService.inviaCredenziali(u.getEmail(), u.getEmail(), password);
    }

    public void registraNuovoOperatore(NuovoOperatoreRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email già in uso: " + request.getEmail());
        }

        // Map base fields manually or use Mapper if we had a method for this DTO.
        // Since we don't carry password in request, we map manually or use a helper.
        // Let's map manually to be safe and simple.
        User user = new User();
        user.setEmail(request.getEmail());
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setDataNascita(request.getDataNascita());
        user.setTelefono(request.getTelefono());
        user.setIndirizzo(request.getIndirizzo());
        user.setAttivo(true);

        // Assign Role OPERATORE (ID 2)
        Role operatoreRole = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Ruolo OPERATORE non trovato"));
        user.setRoles(new HashSet<>());
        user.getRoles().add(operatoreRole);

        // Password from request
        String rawPassword = request.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);

        // Send Email with Raw Password and Link
        emailService.inviaCredenziali(user.getEmail(), user.getEmail(), rawPassword);
    }
}
