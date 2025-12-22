/**
 * 4. 🎮 AuthController
 *    - Riceve la richiesta al metodo login()
 *    - @Valid valida il LoginRequest (ma è già validato dall'interceptor)
 *    - Chiama authControllerService.login()
 */


package it.univaq.swa.soccorsoweb.swa.open;

import it.univaq.swa.soccorsoweb.model.dto.request.LoginRequest;
import it.univaq.swa.soccorsoweb.model.dto.request.UserRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/swa/open/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }
    /** API 1: Login utente
     * Metodo per il login dell'utente
     * @param loginRequest
     * @return ResponseEntity<UserResponse>
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest){
           return ResponseEntity.ok(authService.login(loginRequest));
    }

    /** API 2: Logout utente
     * Metodo per il logout dell'utente
     * @return ResponseEntity<String>
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok(authService.logout());
    }


    /** API di supporto: Registrazione nuovo utente
     * Metodo per la registrazione di un nuovo utente
     * @param userRequest
     * @return ResponseEntity<UserResponse>
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(authService.signUp(userRequest));
    }
}
