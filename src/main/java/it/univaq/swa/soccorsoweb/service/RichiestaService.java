package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.RichiestaSoccorsoMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoUpdateRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.model.entity.Missione;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import it.univaq.swa.soccorsoweb.repository.MissioneRepository;
import it.univaq.swa.soccorsoweb.repository.RichiestaSoccorsoRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RichiestaService {

    private final RichiestaSoccorsoMapper richiestaSoccorsoMapper;
    private final RichiestaSoccorsoRepository richiestaSoccorsoRepository;
    private final MissioneRepository missioneRepository;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public RichiestaService(RichiestaSoccorsoMapper richiestaSoccorsoMapper,
            RichiestaSoccorsoRepository richiestaSoccorsoRepository,
            MissioneRepository missioneRepository, // Add this
            EmailService emailService) {
        this.richiestaSoccorsoMapper = richiestaSoccorsoMapper;
        this.richiestaSoccorsoRepository = richiestaSoccorsoRepository;
        this.missioneRepository = missioneRepository;
        this.emailService = emailService;
    }

    public RichiestaSoccorsoResponse nuovaRichiesta(RichiestaSoccorsoRequest richiestaSoccorsoRequest,
            HttpServletRequest request) throws MessagingException {

        RichiestaSoccorso richiesta = richiestaSoccorsoMapper.toEntity(richiestaSoccorsoRequest);
        richiesta.setIpOrigine(getClientIp(request));
        richiesta.setTokenConvalida(UUID.randomUUID().toString());
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.INVIATA);

        RichiestaSoccorso richiestaSalvata = richiestaSoccorsoRepository.save(richiesta);

        String linkConvalida = baseUrl + "/swa/open/richieste/convalida?token_convalida="
                + richiestaSalvata.getTokenConvalida();

        emailService.inviaEmailConvalida(
                richiestaSalvata.getEmailSegnalante(),
                richiestaSalvata.getNomeSegnalante(),
                linkConvalida);

        return richiestaSoccorsoMapper.toResponse(richiestaSalvata);
    }

    public void convalidaRichiesta(String token) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findByTokenConvalida(token);

        if (richiesta == null) {
            throw new EntityNotFoundException("Token di convalida non valido.");
        }

        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.ATTIVA); // Set to ATTIVA as CONVALIDATA is removed
        richiesta.setConvalidataAt(LocalDateTime.now());
        richiesta.setUpdatedAt(LocalDateTime.now());
        richiestaSoccorsoRepository.save(richiesta);

    }

    public Page<RichiestaSoccorsoResponse> richiesteFiltrate(String stato, Pageable pageable) {
        Page<RichiestaSoccorso> richiesteEntity;

        if ("TUTTE".equalsIgnoreCase(stato)) {
            richiesteEntity = richiestaSoccorsoRepository.findAll(pageable);
        } else {
            RichiestaSoccorso.StatoRichiesta statoEnum = RichiestaSoccorso.StatoRichiesta.valueOf(stato.toUpperCase());
            richiesteEntity = richiestaSoccorsoRepository.findByStato(statoEnum, pageable);
        }

        return richiesteEntity.map(richiestaSoccorsoMapper::toResponse);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    public void eliminaRichiesta(Long id) {
        richiestaSoccorsoRepository.deleteById(id);
    }

    public RichiestaSoccorsoResponse modificaRichiesta(Long id, String nuovoStato) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));
        RichiestaSoccorso.StatoRichiesta statoEnum = RichiestaSoccorso.StatoRichiesta.valueOf(nuovoStato.toUpperCase());
        richiesta.setStato(statoEnum);
        richiesta.setUpdatedAt(LocalDateTime.now());
        RichiestaSoccorso richiestaAggiornata = richiestaSoccorsoRepository.save(richiesta);
        return richiestaSoccorsoMapper.toResponse(richiestaAggiornata);
    }

    public RichiestaSoccorsoResponse dettagliRichiesta(Long id) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));
        return richiestaSoccorsoMapper.toResponse(richiesta);
    }

    public RichiestaSoccorsoResponse annullaRichiesta(Long id) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.IGNORATA); // Use IGNORATA as ANNULLATA is removed
        richiesta.setUpdatedAt(LocalDateTime.now());
        RichiestaSoccorso richiestaAggiornata = richiestaSoccorsoRepository.save(richiesta);
        return richiestaSoccorsoMapper.toResponse(richiestaAggiornata);
    }

    @Transactional
    public RichiestaSoccorsoResponse aggiornaRichiesta(Long id, RichiestaSoccorsoUpdateRequest updateRequest) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));

        // Aggiorna solo i campi forniti (non null)
        if (updateRequest.getDescrizione() != null) {
            richiesta.setDescrizione(updateRequest.getDescrizione());
        }
        if (updateRequest.getIndirizzo() != null) {
            richiesta.setIndirizzo(updateRequest.getIndirizzo());
        }
        if (updateRequest.getLatitudine() != null) {
            richiesta.setLatitudine(updateRequest.getLatitudine());
        }
        if (updateRequest.getLongitudine() != null) {
            richiesta.setLongitudine(updateRequest.getLongitudine());
        }
        if (updateRequest.getNomeSegnalante() != null) {
            richiesta.setNomeSegnalante(updateRequest.getNomeSegnalante());
        }
        if (updateRequest.getEmailSegnalante() != null) {
            richiesta.setEmailSegnalante(updateRequest.getEmailSegnalante());
        }
        if (updateRequest.getFotoUrl() != null) {
            richiesta.setFotoUrl(updateRequest.getFotoUrl());
        }
        if (updateRequest.getTelefonoSegnalante() != null) {
            richiesta.setTelefonoSegnalante(updateRequest.getTelefonoSegnalante());
        }
        if (updateRequest.getStato() != null) {
            RichiestaSoccorso.StatoRichiesta statoEnum = RichiestaSoccorso.StatoRichiesta
                    .valueOf(updateRequest.getStato().toUpperCase());
            richiesta.setStato(statoEnum);
        }
        if (updateRequest.getLivelloSuccesso() != null && richiesta.getMissione() != null) {
            // Redirect level update to Missione
            Missione missione = richiesta.getMissione();
            missione.setLivelloSuccesso(updateRequest.getLivelloSuccesso());
            missioneRepository.save(missione);
        }

        richiesta.setUpdatedAt(LocalDateTime.now());
        RichiestaSoccorso richiestaAggiornata = richiestaSoccorsoRepository.save(richiesta);
        return richiestaSoccorsoMapper.toResponse(richiestaAggiornata);
    }

    public RichiestaSoccorsoResponse valutaRichiesta(Long id, Integer livelloSuccesso) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));

        if (richiesta.getMissione() != null) {
            Missione missione = richiesta.getMissione();
            missione.setLivelloSuccesso(livelloSuccesso);
            missioneRepository.save(missione);
        } else {
            // Handle case where request has no mission (unlikely if strictly followed
            // process, but possible)
            // For now throw/log or ignore?
            // Ideally we shouldn't rate a request without mission.
            throw new IllegalStateException("Impossibile valutare una richiesta senza missione associata");
        }

        return richiestaSoccorsoMapper.toResponse(richiesta);
    }

    public List<RichiestaSoccorsoResponse> richiesteValutateNegative() {
        // This needs to join with Missione to check level.
        // Since RichiestaSoccorso has one Missione, we can filter in Java or use custom
        // query.
        // Let's assume we want requests where Missione.livelloSuccesso <= 2
        List<Missione> missioniNegative = missioneRepository.findAll().stream()
                .filter(m -> m.getLivelloSuccesso() != null && m.getLivelloSuccesso() <= 2)
                .collect(Collectors.toList());

        List<RichiestaSoccorso> richieste = missioniNegative.stream()
                .map(Missione::getRichiesta)
                .collect(Collectors.toList());

        return richiestaSoccorsoMapper.toResponseList(richieste);
    }
}
