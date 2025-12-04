package it.univaq.swa.soccorsoweb.controller;

import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.service.RichiestaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/swa/open/richiesta")
public class RichiestaController {

    public RichiestaService richiestaService;

    public RichiestaController(RichiestaService richiestaService) {
        this.richiestaService = richiestaService;
    }

    @PostMapping("/nuova-richiesta")
    public ResponseEntity<RichiestaSoccorsoResponse> nuovaRichiesta(@Valid @RequestBody RichiestaSoccorsoRequest richiestaSoccorsoRequest) {

        RichiestaSoccorsoResponse response = richiestaService.nuovaRichiesta(richiestaSoccorsoRequest);
        return ResponseEntity.created(URI.create("/swa/open/richiesta/nuova-richiesta/" + response.getId()))
                .body(response);

    }
}
