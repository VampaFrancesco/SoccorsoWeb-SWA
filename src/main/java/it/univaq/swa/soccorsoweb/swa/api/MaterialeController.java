package it.univaq.swa.soccorsoweb.swa.api;

import it.univaq.swa.soccorsoweb.model.dto.request.MaterialeRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MaterialeResponse;
import it.univaq.swa.soccorsoweb.service.MaterialeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/swa/api/materiali")
@RequiredArgsConstructor
public class MaterialeController {

    private final MaterialeService materialeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public List<MaterialeResponse> getAll() {
        return materialeService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public MaterialeResponse getById(@PathVariable Long id) {
        return materialeService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORE')")
    public ResponseEntity<MaterialeResponse> create(@Valid @RequestBody MaterialeRequest request) {
        MaterialeResponse created = materialeService.create(request);
        return ResponseEntity.created(URI.create("/swa/api/materiali/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
