package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.request.NuovoOperatoreRequest;
import it.univaq.swa.soccorsoweb.service.CredenzialiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("ApiAuthController")
@RequestMapping("/swa/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CredenzialiService credenzialiService;

    @PostMapping("/registrazione")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> registrazione(@Valid @RequestBody NuovoOperatoreRequest request) {
        credenzialiService.registraNuovoOperatore(request);
        return ResponseEntity.ok().build();
    }
}
