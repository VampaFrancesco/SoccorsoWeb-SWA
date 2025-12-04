/**
 * 4. 🎮 AuthController
 *    - Riceve la richiesta al metodo login()
 *    - @Valid valida il LoginRequest (ma è già validato dall'interceptor)
 *    - Chiama authControllerService.login()
 */


package it.univaq.swa.soccorsoweb.controller;

import it.univaq.swa.soccorsoweb.model.dto.request.LoginRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/swa/open/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest){
           return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok(authService.logout());
    }
}
