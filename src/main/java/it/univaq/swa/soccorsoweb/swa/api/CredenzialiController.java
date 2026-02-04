package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.response.CredenzialiResponse;
import it.univaq.swa.soccorsoweb.service.CredenzialiService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/swa/api/credenziali")
public class CredenzialiController {

    private final CredenzialiService credenzialiService;

    @GetMapping("") // credenziali?type=operatore oppure type=admin
    public ResponseEntity<Void> nuoveCredenziali(@RequestParam String type) {
        return ResponseEntity.notFound().build();
    }

}
