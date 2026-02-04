package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.CredenzialiResponse;
import it.univaq.swa.soccorsoweb.service.CredenzialiService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/swa/api/credenziali")
public class CredenzialiController {

    private final CredenzialiService credenzialiService;

    @PostMapping() // credenziali?type=operatore oppure type=admin
    public ResponseEntity<Void> nuoveCredenziali(@RequestParam String type) {
        credenzialiService.generaCredenziali(type);
        return ResponseEntity.ok().build();
    }

}
