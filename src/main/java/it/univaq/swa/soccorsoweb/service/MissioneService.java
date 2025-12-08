package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.MissioneMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.MissioneRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
import it.univaq.swa.soccorsoweb.model.entity.Missione;
import it.univaq.swa.soccorsoweb.model.entity.MissioneOperatore;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import it.univaq.swa.soccorsoweb.model.entity.User;
import it.univaq.swa.soccorsoweb.repository.MissioneRepository;
import it.univaq.swa.soccorsoweb.repository.RichiestaSoccorsoRepository;
import it.univaq.swa.soccorsoweb.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Data
@Service
public class MissioneService {

    private final MissioneRepository missioneRepository;
    private final MissioneMapper missioneMapper;
    private final RichiestaSoccorsoRepository richiestaSoccorsoRepository;
    private final UserRepository userRepository;

    @Transactional
    public MissioneResponse assegnaMissione(MissioneRequest missioneRequest) {
        // Carica la richiesta di soccorso
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(missioneRequest.getRichiestaId())
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + missioneRequest.getRichiestaId()));

        // Carica il caposquadra
        User caposquadra = userRepository.findById(missioneRequest.getCaposquadraId())
                .orElseThrow(() -> new EntityNotFoundException("Caposquadra non trovato con ID: " + missioneRequest.getCaposquadraId()));

        // Crea la missione usando il mapper
        Missione missione = missioneMapper.toEntity(missioneRequest);
        missione.setRichiesta(richiesta);
        missione.setCaposquadra(caposquadra);
        missione.setCreatedAt(LocalDateTime.now());

        // Gestisce gli operatori se presenti
        if (missioneRequest.getOperatoriIds() != null && !missioneRequest.getOperatoriIds().isEmpty()) {
            Set<MissioneOperatore> missioneOperatori = new HashSet<>();
            for (Long operatoreId : missioneRequest.getOperatoriIds()) {
                User operatore = userRepository.findById(operatoreId)
                        .orElseThrow(() -> new EntityNotFoundException("Operatore non trovato con ID: " + operatoreId));

                MissioneOperatore missioneOperatore = new MissioneOperatore();
                MissioneOperatore.MissioneOperatoreId id = new MissioneOperatore.MissioneOperatoreId();
                missioneOperatore.setId(id);
                missioneOperatore.setMissione(missione);
                missioneOperatore.setOperatore(operatore);
                missioneOperatori.add(missioneOperatore);
            }
            missione.setMissioneOperatori(missioneOperatori);
        }

        // Aggiorna lo stato della richiesta
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.IN_CORSO);

        // Salva la missione
        Missione missioneSalvata = missioneRepository.save(missione);
        return missioneMapper.toResponse(missioneSalvata);
    }

    public List<MissioneResponse> missioniValutateNegative() {
        return missioneMapper.toResponseList(missioneRepository.findAllByLivelloSuccesso());
    }

    @Transactional
    public MissioneResponse modificaMissione(Long id, String stato) {
        log.info("🔵 INIZIO modificaMissione - ID: {}, Stato ricevuto: '{}'", id, stato);

        try {
            // 1. Cerca la missione
            log.info("🔍 Cerco missione con ID: {}", id);
            Missione missione = missioneRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("❌ Missione non trovata con ID: {}", id);
                        return new EntityNotFoundException("Missione non trovata con ID: " + id);
                    });
            log.info("✅ Missione trovata: ID={}, Stato attuale={}", missione.getId(), missione.getStato());

            // 2. Converte lo stato in maiuscolo
            String statoUpper = stato.toUpperCase();
            log.info("🔄 Stato convertito in maiuscolo: '{}'", statoUpper);

            // 3. Valida che lo stato esista nell'enum
            Missione.StatoMissione nuovoStato;
            try {
                nuovoStato = Missione.StatoMissione.valueOf(statoUpper);
                log.info("✅ Stato validato correttamente: {}", nuovoStato);
            } catch (IllegalArgumentException e) {
                log.error("❌ Stato non valido: '{}'. Valori ammessi: IN_CORSO, CHIUSA, FALLITA", stato);
                throw new IllegalArgumentException("Stato non valido: " + stato + ". Valori ammessi: IN_CORSO, CHIUSA, FALLITA");
            }

            // 4. Imposta il nuovo stato
            log.info("📝 Imposto nuovo stato: {} -> {}", missione.getStato(), nuovoStato);
            missione.setStato(nuovoStato);
            missione.setUpdatedAt(LocalDateTime.now());

            // 5. Se la missione viene chiusa o fallita, imposta anche la data di fine
            if (nuovoStato == Missione.StatoMissione.CHIUSA || nuovoStato == Missione.StatoMissione.FALLITA) {
                log.info("⏰ Imposto fineAt perché stato è: {}", nuovoStato);
                missione.setFineAt(LocalDateTime.now());

                // 6. Aggiorna anche lo stato della richiesta
                RichiestaSoccorso richiesta = missione.getRichiesta();
                if (richiesta != null) {
                    log.info("🔗 Aggiorno stato richiesta collegata (ID: {})", richiesta.getId());
                    RichiestaSoccorso.StatoRichiesta statoRichiesta =
                        nuovoStato == Missione.StatoMissione.CHIUSA
                            ? RichiestaSoccorso.StatoRichiesta.CHIUSA
                            : RichiestaSoccorso.StatoRichiesta.IGNORATA;
                    richiesta.setStato(statoRichiesta);
                    log.info("✅ Stato richiesta impostato a: {}", statoRichiesta);
                } else {
                    log.warn("⚠️ Nessuna richiesta collegata alla missione");
                }
            }

            // 7. Salva la missione
            log.info("💾 Salvo la missione aggiornata...");
            Missione missioneSalvata = missioneRepository.save(missione);
            log.info("✅ Missione salvata con successo");

            // 8. Converte in response
            log.info("🔄 Converto la missione in MissioneResponse...");
            MissioneResponse response = missioneMapper.toResponse(missioneSalvata);
            log.info("✅ FINE modificaMissione - Response creata con successo");

            return response;

        } catch (Exception e) {
            log.error("❌❌❌ ERRORE in modificaMissione: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void eliminaMissione(Long id) {
        Missione missione = missioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Missione non trovata con ID: " + id));

        // Ripristina lo stato della richiesta
        RichiestaSoccorso richiesta = missione.getRichiesta();
        if (richiesta != null) {
            richiesta.setStato(RichiestaSoccorso.StatoRichiesta.CONVALIDATA);
        }

        missioneRepository.deleteById(id);
    }

}
