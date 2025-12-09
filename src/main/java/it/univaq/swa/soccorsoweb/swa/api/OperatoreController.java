package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.UserResponse;
import it.univaq.swa.soccorsoweb.service.OperatoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/swa/api/operatore")
public class OperatoreController {

    private final OperatoreService operatoreService;

    public OperatoreController(OperatoreService operatoreService) {
        this.operatoreService = operatoreService;
    }

    /** API 6: Visualizza operatori disponibili
     * Metodo per visualizzare gli operatori disponibili
     * @return ResponseEntity<List<UserResponse>>
     */
    @GetMapping("/disponibile")
    @PreAuthorize("hasAnyRole('OPERATORE', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> operatoreDisponibile() {
        return ResponseEntity.ok().body(operatoreService.operatoreDisponibile());
    }

    /** API 12: Visualizza dettagli operatore
     * Metodo per visualizzare i dettagli di un operatore
     * @return ResponseEntity<UserResponse>
     */
    @GetMapping("/dettagli-operatore/{id}")
    @PreAuthorize("hasAnyRole('OPERATORE', 'ADMIN')")
    public ResponseEntity<UserResponse> dettagliOperatore(@PathVariable Long id) throws RoleNotFoundException {
        return ResponseEntity.ok().body(operatoreService.dettagliOperatore(id));
    }


}
