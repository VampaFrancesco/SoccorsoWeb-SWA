package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.request.MissioneRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.service.MissioneService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swa/api/missione")
@Data
public class MissioneController {

    private final MissioneService missioneService;

    /**
     * API 5: Visualizza missioni non positive < 5
     * @return ResponseEntity<List<MissioneResponse>>
     */
    @GetMapping("/missioni-non-positive")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<List<MissioneResponse>> missioniValutateNegative() {
        return ResponseEntity.ok().body(missioneService.missioniValutateNegative());
    }


    /** API 7: Inserimento di una nuova missione
     * Metodo per l'inserimento di una nuova missione a cui viene legata una richiesta di soccorso
     * @param missioneRequest MissioneRequest
     * @return ResponseEntity<MissioneResponse>
     */
    @PostMapping("/inserisci-missione")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> inserisciMissione(
            @Valid @RequestBody MissioneRequest missioneRequest) {
        return ResponseEntity.ok().body(missioneService.inserisciMissione(missioneRequest));
    }


    /** API 8: Chiusura missione
     * Metodo per la chiusura di una missione
     *
     *
     */
    @PutMapping("/chiudi-missione/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> chiudiMissione(@PathVariable Long id) {
        return ResponseEntity.ok().body(missioneService.chiudiMissione(id));
    }


    /** API 9: Annulla missione
     * Metodo per l'annullamento di una missione
     * @param id ID della missione
     * @return ResponseEntity<MissioneResponse>
     */
    @PutMapping("/annulla-missione/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MissioneResponse> annullaMissione(@PathVariable Long id) {
        return ResponseEntity.ok().body(missioneService.annullaMissione(id));
    }

    /** API 10: Dettagli missione
     * Metodo per ottenere i dettagli di una missione tramite ID
     * @param id ID della missione
     * @return ResponseEntity<MissioneResponse>
     */
    @GetMapping("/dettagli-missione/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATORE')")
    public ResponseEntity<MissioneResponse> dettagliMissione(@PathVariable Long id){
        return ResponseEntity.ok().body(missioneService.dettagliMissione(id));
    }


    /**
     * API 13: Liste missioni di uno specifico operatore
     * @param id
     * @return
     */
    @GetMapping("/missioni-operatore/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATORE')")
    public ResponseEntity<List<MissioneResponse>> missioniOperatore(@PathVariable Long id){
        return ResponseEntity.ok().body(missioneService.missioniOperatore(id));
    }

// ---------------------------------------------------------------------- API SUPPORTO ----------------------------------------------------------------------
    /** API di supporto: modifica stato missione
     * Metodo per la modifica dello stato di una missione
     * @param id ID della missione
     * @param nuovoStato Nuovo stato (IN_CORSO, CHIUSA, FALLITA)
     * @return ResponseEntity<MissioneResponse>
     */
    @PutMapping("/modifica-stato/{id}/{nuovoStato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> modificaStatoMissione(
            @PathVariable Long id,
            @PathVariable String nuovoStato) {
            MissioneResponse response = missioneService.modificaMissione(id, nuovoStato);

            if (response != null) {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.noContent().build();
        }

    /**
     * API di supporto: elimina missione
     * Metodo per eliminare una missione tramite ID
     * @return 204 No Content
     */
    @DeleteMapping("/elimina-missione/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminaMissione(@PathVariable Long id) {
        missioneService.eliminaMissione(id);
        return ResponseEntity.noContent().build();
    }

    /** API di supporto: valuta missione
     * Metodo per valutare una missione al termine della stessa
     * @param id ID della missione
     * @param livelloSuccesso Livello di successo (1-10)
     * @return ResponseEntity<MissioneResponse>
     */
    @PutMapping("/valuta-missione/{id}/{livelloSuccesso}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> valutaMissione(
            @PathVariable Long id,
            @PathVariable Integer livelloSuccesso) {
        return ResponseEntity.ok().body(missioneService.valutaMissione(id, livelloSuccesso));
    }



}
