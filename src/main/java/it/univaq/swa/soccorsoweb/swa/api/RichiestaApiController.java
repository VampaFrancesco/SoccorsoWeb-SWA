package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.service.RichiestaService;
import jakarta.mail.MessagingException;
import jakarta.websocket.server.PathParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * API 5: Visualizza richieste non positive < 5
     * @return ResponseEntity<List<RichiestaSoccorsoResponse>>
     */
    @GetMapping("/richieste-non-positive")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<List<RichiestaSoccorsoResponse>> richiesteValutateNegative() {
        return ResponseEntity.ok().body(richiestaService.richiesteValutateNegative());
    }


    /** API di supporto: modifica stato richiesta
     * Metodo per la modifica dello stato di una richiesta di soccorso
     * @param id
     * @param nuovoStato
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    @GetMapping("/modifica-stato/{id}/{nuovoStato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<RichiestaSoccorsoResponse> modificaStatoRichiesta(
            @PathVariable Long id,
            @PathVariable String nuovoStato) throws MessagingException {
        RichiestaSoccorsoResponse response = richiestaService.chiudiSoccorso(id, nuovoStato);
        if (response != null) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.noContent().build();
    }
}
