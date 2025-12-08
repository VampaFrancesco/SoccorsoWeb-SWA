package it.univaq.swa.soccorsoweb.swa.api;

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
@RequestMapping("/swa/api/missione")
@Data
public class MissioneController {

    private final MissioneService missioneService;


    @PostMapping("/assegna-missione")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> assegnaMissione(@Valid @RequestBody MissioneRequest missioneRequest) {
        return ResponseEntity.ok().body(missioneService.assegnaMissione(missioneRequest));
    }

    /**
     * API 5: Visualizza missioni non positive < 5
     * @return ResponseEntity<List<MissioneResponse>>
     */
    @GetMapping("/missioni-non-positive")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<List<MissioneResponse>> missioniValutateNegative() {
        return ResponseEntity.ok().body(missioneService.missioniValutateNegative());
    }


    /** API di supporto: modifica stato richiesta
     * Metodo per la modifica dello stato di una richiesta di soccorso
     * @param id ID della missione
     * @param nuovoStato Nuovo stato (IN_CORSO, CHIUSA, FALLITA)
     * @return ResponseEntity<MissioneResponse>
     */
    @PutMapping("/modifica-stato/{id}/{nuovoStato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MissioneResponse> modificaStatoMissione(
            @PathVariable Long id,
            @PathVariable String nuovoStato) {
        log.info("📞 API CHIAMATA: PUT /modifica-stato/{}/{}", id, nuovoStato);

        try {
            MissioneResponse response = missioneService.modificaMissione(id, nuovoStato);

            if (response != null) {
                log.info("✅ Risposta OK - Missione ID: {}, Nuovo stato: {}", id, nuovoStato);
                return ResponseEntity.ok().body(response);
            }

            log.warn("⚠️ Response null - Ritorno 204 No Content");
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("❌ ERRORE nel controller: {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/elimina-missione/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminaMissione(@PathVariable Long id) {
        missioneService.eliminaMissione(id);
        return ResponseEntity.noContent().build();
    }

}
