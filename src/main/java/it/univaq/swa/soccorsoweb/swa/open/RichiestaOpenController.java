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
@RequestMapping("/swa/open/richieste")
public class RichiestaOpenController {

    private final RichiestaService richiestaService;

    public RichiestaOpenController(RichiestaService richiestaService) {
        this.richiestaService = richiestaService;
    }

    /**
     * API 2: Inserisci nuova richiesta di soccorso
     * @param richiestaSoccorsoRequest
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    //POST /swa/open/richieste
    @PostMapping
    public ResponseEntity<RichiestaSoccorsoResponse> nuovaRichiesta(
            @Valid @RequestBody RichiestaSoccorsoRequest richiestaSoccorsoRequest,
            HttpServletRequest request) throws MessagingException {

        RichiestaSoccorsoResponse response = richiestaService.nuovaRichiesta(richiestaSoccorsoRequest, request);

        return ResponseEntity
                .created(URI.create("/swa/open/richiesta/" + response.getId()))
                .body(response);
    }

    /**
     * API 3: Convalida richiesta di soccorso
     * @param token_convalida
     * @return ResponseEntity<Map<String, Object>>
     */
    // GET /swa/open/richieste/convalida?token_convalida=...
    @GetMapping("/convalida")
    public ResponseEntity<Map<String, Object>> convalidaRichiesta(
            @RequestParam("token_convalida") String token_convalida) {

        richiestaService.convalidaRichiesta(token_convalida);

        return ResponseEntity.ok(Map.of(
                "message", "✅ La tua richiesta di soccorso è stata convalidata con successo!",
                "timestamp", java.time.LocalDateTime.now()
        ));
    }
}
