package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.service.RichiestaService;
import jakarta.mail.MessagingException;
import jakarta.websocket.server.PathParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swa/api/richiesta")
public class RichiestaApiController {

    public final RichiestaService richiestaService;

    public RichiestaApiController(RichiestaService richiestaService) {
        this.richiestaService = richiestaService;

    }

    /**
     * API 4: Visualizza richieste filtrate per stato
     * @param stato
     * @return ResponseEntity<List<RichiestaSoccorsoResponse>>
     */
    @GetMapping("/visualizza-richieste-filtrate/{stato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<List<RichiestaSoccorsoResponse>> richiesteFiltrate(@PathVariable(value = "stato") String stato) {
        List<RichiestaSoccorsoResponse> response = richiestaService.richiesteFiltrate(stato);
        log.info("Lo stato è {}", stato);
        if(!(response == null)) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.noContent().build();
    }




}
