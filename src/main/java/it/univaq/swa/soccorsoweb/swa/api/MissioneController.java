package it.univaq.swa.soccorsoweb.swa.api;

import io.swagger.v3.oas.annotations.Operation;
import it.univaq.swa.soccorsoweb.model.dto.request.MissioneRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.service.MissioneService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swa/api/missioni")
@Data
public class MissioneController {

    private final MissioneService missioneService;

    /**
     * API 5: Visualizza missioni non positive < 5
     * @return ResponseEntity<List<MissioneResponse>>
     */
    // GET /swa/api/missioni
    @GetMapping("/non-positive")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<List<MissioneResponse>> missioniValutateNegative() {
        return ResponseEntity.ok().body(missioneService.missioniValutateNegative());
    }


    /** API 7: Inserimento di una nuova missione
     * Metodo per l'inserimento di una nuova missione a cui viene legata una richiesta di soccorso
     * @param missioneRequest MissioneRequest
     * @return ResponseEntity<MissioneResponse>
     */
    // POST /swa/api/missioni
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> inserisciMissione(
            @Valid @RequestBody MissioneRequest missioneRequest) {
        return ResponseEntity.ok().body(missioneService.inserisciMissione(missioneRequest));
    }


    /** API 8: Chiusura missione
     * Metodo per la chiusura di una missione
     */
    // PATCH /swa/api/missioni/{id}/chiusura
    @PatchMapping("/{id}/chiusura")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> chiudiMissione(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(missioneService.chiudiMissione(id));
    }


    /** API 10: Dettagli missione
     * Metodo per ottenere i dettagli di una missione tramite ID
     * @param id ID della missione
     * @return ResponseEntity<MissioneResponse>
     */
    // GET /swa/api/missioni/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATORE')")
    public ResponseEntity<MissioneResponse> dettagliMissione(@PathVariable Long id){
        return ResponseEntity.ok().body(missioneService.dettagliMissione(id));
    }




// ---------------------------------------------------------------------- API SUPPORTO ----------------------------------------------------------------------


    /** API di supporto: modifica stato missione
     * Metodo per la modifica dello stato di una missione
     * @param id ID della missione
     * @param nuovoStato Nuovo stato (IN_CORSO, CHIUSA, FALLITA)
     * @return ResponseEntity<MissioneResponse>
     */
    @Operation

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
    // DELETE /swa/api/missioni/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminaMissione(@PathVariable Long id) {
        missioneService.eliminaMissione(id);
        return ResponseEntity.noContent().build();
    }

    /** API di supporto: valuta missione
     * Metodo per valutare una missione al termine della stessa
     * @param id ID della missione
     * @param valutazione Livello di successo (1-10)
     * @return ResponseEntity<MissioneResponse>
     */
    // PATCH /swa/api/missioni/{id}/valutazione
    @PatchMapping("/{id}/valutazione")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> valutaMissione(
            @PathVariable Long id,
            @RequestParam("valutazione") Integer valutazione) {
        return ResponseEntity.ok().body(missioneService.valutaMissione(id, valutazione));
    }



}
