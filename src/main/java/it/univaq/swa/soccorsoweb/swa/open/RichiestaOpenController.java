package it.univaq.swa.soccorsoweb.swa.open;

import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.service.RichiestaService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/swa/open/richiesta")
public class RichiestaOpenController {

    private final RichiestaService richiestaService;

    public RichiestaOpenController(RichiestaService richiestaService) {
        this.richiestaService = richiestaService;
    }

    /**
     * API 2: Invia nuova richiesta di soccorso
     * @param richiestaSoccorsoRequest
     * @param request
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     * @throws MessagingException
     */
    @PostMapping("/nuova-richiesta")
    public ResponseEntity<RichiestaSoccorsoResponse> nuovaRichiesta(
            @Valid @RequestBody RichiestaSoccorsoRequest richiestaSoccorsoRequest,
            HttpServletRequest request) throws MessagingException {

        RichiestaSoccorsoResponse response = richiestaService.nuovaRichiesta(richiestaSoccorsoRequest, request);

        // ✅ Location corretto senza "/nuova-richiesta"
        return ResponseEntity
                .created(URI.create("/swa/open/richiesta/" + response.getId()))
                .body(response);
    }
    /**
     * API 3: Convalida richiesta di soccorso
     * @param id
     * @param token
     * @return ResponseEntity<Map<String, Object>>
     */
    @GetMapping("/{id}/convalida/{token}")
    public ResponseEntity<Map<String, Object>> convalidaRichiesta(
            @PathVariable Long id,
            @PathVariable String token) {

        richiestaService.convalidaRichiesta(id, token);

        return ResponseEntity.ok(Map.of(
                "message", "✅ La tua richiesta di soccorso è stata convalidata con successo!",
                "id", id,
                "timestamp", java.time.LocalDateTime.now()
        ));
    }
}
