package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.service.MissioneService;
import it.univaq.swa.soccorsoweb.service.OperatoreService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/swa/api/operatori")
public @Data class OperatoreController {

    private final OperatoreService operatoreService;
    private final MissioneService missioneService;


    /** API 6: Visualizza operatori disponibili
     * Metodo per visualizzare gli operatori disponibili
     * @return ResponseEntity<List<UserResponse>>
     */
    // GET /swa/api/operatori?disponibili=true
    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATORE', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> operatori(
            @RequestParam(required = false, defaultValue = "true") boolean disponibile) {

        return ResponseEntity.ok(operatoreService.operatoreDisponibile(disponibile));
    }

    /** API 12: Visualizza dettagli operatore
     * Metodo per visualizzare i dettagli di un operatore
     * @return ResponseEntity<UserResponse>
     */
    // GET /swa/api/operatori/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATORE', 'ADMIN')")
    public ResponseEntity<UserResponse> dettagliOperatore(@PathVariable Long id){
        return ResponseEntity.ok().body(operatoreService.dettagliOperatore(id));
    }

    /**
     * API 13: Liste missioni di uno specifico operatore
     * @param id
     * @return
     */
    // GET /swa/api/operatori/{id}/missioni
    @GetMapping("/{id}/missioni")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATORE')")
    public ResponseEntity<List<MissioneResponse>> missioniOperatore(@PathVariable Long id){
        return ResponseEntity.ok().body(missioneService.missioniOperatore(id));
    }


}
