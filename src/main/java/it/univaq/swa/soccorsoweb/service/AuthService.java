/**

 5. 🔧 AuthControllerService
 a) authenticationManager.authenticate()
 - Chiama il UserDetailsService per caricare l'utente dal DB
 - Confronta la password in chiaro con l'hash usando BCryptPasswordEncoder
 - Se PASSWORD SBAGLIATA → lancia BadCredentialsException 💥
 - Se OK → crea l'oggetto Authentication ✅

 b) Genera il token JWT con JWTUtil

 c) Carica i dati completi dell'utente dal DB

 d) Crea e restituisce UserResponse con il token

 ↓

 6. ✅ Risposta al CLIENT
 - 200 OK + UserResponse con token JWT
 */



package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.UserMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.LoginRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.model.entity.User;
import it.univaq.swa.soccorsoweb.repository.UserRepository;
import it.univaq.swa.soccorsoweb.security.jwt.JWTUtil;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    // Constructor Injection
    public AuthService(AuthenticationManager authenticationManager,
                       JWTUtil jwtUtil,
                       UserRepository userRepository,
                       UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    public UserResponse login(@NotNull LoginRequest loginRequest) {

        if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email è obbligatoria");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("La password è obbligatoria");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        UserResponse response = userMapper.toResponse(user);

        response.setToken(token);

        return response;
    }

    public String logout() {
        // In JWT stateless authentication, logout is typically handled on the client side
        // by simply deleting the token. However, if you want to implement server-side
        // logout logic (e.g., token blacklisting), you can do so here.

        // For now, we'll just return a simple response indicating logout was successful.
        return "Logout successful";
    }
}
