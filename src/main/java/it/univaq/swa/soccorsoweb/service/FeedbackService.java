package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.RichiestaSoccorsoMapper;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import it.univaq.swa.soccorsoweb.repository.RichiestaSoccorsoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class FeedbackService {

    private final RichiestaSoccorsoRepository richiestaSoccorsoRepository;
    private final RichiestaSoccorsoMapper richiestaSoccorsoMapper;

    public FeedbackService(RichiestaSoccorsoRepository richiestaSoccorsoRepository, RichiestaSoccorsoMapper richiestaSoccorsoMapper) {
        this.richiestaSoccorsoMapper = richiestaSoccorsoMapper;
        this.richiestaSoccorsoRepository = richiestaSoccorsoRepository;
    }

    @Transactional
    public RichiestaSoccorsoResponse inviaFeedback(Long idSoccorso, Integer livello) {
        log.info("📝 Ricevuto feedback per richiesta ID: {} con livello: {}", idSoccorso, livello);

        // Validazione livello
        if (livello < 1 || livello > 10) {
            throw new IllegalArgumentException("Il livello di successo deve essere compreso tra 1 e 10");
        }

        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(idSoccorso)
                .orElseThrow(() -> new IllegalArgumentException("Richiesta di soccorso non trovata con ID: " + idSoccorso));

        // Verifica che la richiesta sia chiusa
        if (richiesta.getStato() != RichiestaSoccorso.StatoRichiesta.CHIUSA) {
            throw new IllegalStateException("Puoi valutare solo richieste chiuse. Stato attuale: " + richiesta.getStato());
        }

        richiesta.setLivelloSuccesso(livello);
        richiesta.setValutataAt(LocalDateTime.now());

        RichiestaSoccorso richiestaSalvata = richiestaSoccorsoRepository.save(richiesta);
        log.info("✅ Feedback salvato con successo per richiesta ID: {}", idSoccorso);

        return richiestaSoccorsoMapper.toResponse(richiestaSalvata);
    }
}
