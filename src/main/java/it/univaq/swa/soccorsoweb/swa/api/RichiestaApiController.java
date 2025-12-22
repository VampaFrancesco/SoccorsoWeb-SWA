package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.service.RichiestaService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swa/api/richieste")
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
    // GET /swa/api/richieste?stato=...
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<List<RichiestaSoccorsoResponse>> richiesteFiltrate(@RequestParam("stato") String stato) {
        List<RichiestaSoccorsoResponse> response = richiestaService.richiesteFiltrate(stato);
        if(!(response == null)) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.noContent().build();
    }

    /** API 9: Annulla richiesta di soccorso
     * Metodo per l'annullamento di una richiesta di soccorso
     * @param id ID della richiesta di soccorso
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    // PATCH /swa/api/richieste/{id}/annullamento
    @PatchMapping("/{id}/annullamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RichiestaSoccorsoResponse> annullaRichiestaSoccorso(@PathVariable Long id) {
        return ResponseEntity.ok().body(richiestaService.annullaRichiesta(id));
    }


    /** API 11: Dettagli richiesta di soccorso
     * Metodo per visualizzare i dettagli di una richiesta di soccorso
     * @param id ID della richiesta di soccorso
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    // GET /swa/api/richieste/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<RichiestaSoccorsoResponse> dettagliRichiesta(@PathVariable Long id) {
        return ResponseEntity.ok().body(richiestaService.dettagliRichiesta(id));
    }



    // ------------------------------------------ API SUPPORTO ------------------------------------------

    // DELETE /swa/api/richieste/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminaMissione(@PathVariable Long id) {
        richiestaService.eliminaRichiesta(id);
        return ResponseEntity.noContent().build();
    }

    /** API di supporto: modifica stato richiesta
     * Metodo per la modifica dello stato di una richiesta di soccorso
     * @param id ID della missione
     * @param stato Nuovo stato ('INVIATA','ATTIVA','CONVALIDATA','IN_CORSO','CHIUSA','IGNORATA')
     * @return ResponseEntity<RichiestaSoccorsoResponse>
     */
    // PATCH /swa/api/richieste/{id}/stato
    @PatchMapping("/{id}/stato")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<RichiestaSoccorsoResponse> modificaStatoRichiesta(
            @PathVariable Long id,
            @RequestParam("stato") String stato) {
        RichiestaSoccorsoResponse response = richiestaService.modificaRichiesta(id, stato);

        if (response != null) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.noContent().build();
    }
}
