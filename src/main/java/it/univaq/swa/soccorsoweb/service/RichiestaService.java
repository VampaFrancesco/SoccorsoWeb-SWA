package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.RichiestaSoccorsoMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import it.univaq.swa.soccorsoweb.repository.RichiestaSoccorsoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RichiestaService {

    private final RichiestaSoccorsoMapper richiestaSoccorsoMapper;
    private final RichiestaSoccorsoRepository richiestaSoccorsoRepository;

    public RichiestaService(RichiestaSoccorsoMapper richiestaSoccorsoMapper,RichiestaSoccorsoRepository richiestaSoccorsoRepository) {
        this.richiestaSoccorsoMapper = richiestaSoccorsoMapper;
        this.richiestaSoccorsoRepository = richiestaSoccorsoRepository;
    }

    public RichiestaSoccorsoResponse nuovaRichiesta(RichiestaSoccorsoRequest richiestaSoccorsoRequest) {
        log.info("Creazione nuova richiesta di soccorso da: {}", richiestaSoccorsoRequest.getNomeSegnalante());
        
        RichiestaSoccorso richiesta = richiestaSoccorsoMapper.toEntity(richiestaSoccorsoRequest);
        RichiestaSoccorso richiestaSalvata = richiestaSoccorsoRepository.save(richiesta);
        
        log.info("Richiesta di soccorso creata con ID: {}", richiestaSalvata.getId());
        return richiestaSoccorsoMapper.toResponse(richiestaSalvata);
    }
}
