package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
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

    /** API di supporto: modifica stato richiesta
     * Metodo per la modifica dello stato di una richiesta di soccorso
     * @param id ID della missione
     * @param nuovoStato Nuovo stato ('INVIATA','ATTIVA','CONVALIDATA','IN_CORSO','CHIUSA','IGNORATA')
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    @PutMapping("/modifica-stato/{id}/{nuovoStato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<RichiestaSoccorsoResponse> modificaStatoRichiesta(
            @PathVariable Long id,
            @PathVariable String nuovoStato) {
        RichiestaSoccorsoResponse response = richiestaService.modificaRichiesta(id, nuovoStato);

        if (response != null) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.noContent().build();
    }


    /** API 11: Dettagli richiesta di soccorso
     * Metodo per visualizzare i dettagli di una richiesta di soccorso
     * @param id ID della richiesta di soccorso
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    @GetMapping("/dettagli-richiesta/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<RichiestaSoccorsoResponse> dettagliRichiesta(@PathVariable Long id) {
        return ResponseEntity.ok().body(richiestaService.dettagliRichiesta(id));
    }



    // ------------------------------------------ API SUPPORTO ------------------------------------------

    @DeleteMapping("/elimina-missione/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminaMissione(@PathVariable Long id) {
        richiestaService.eliminaRichiesta(id);
        return ResponseEntity.noContent().build();
    }




}
