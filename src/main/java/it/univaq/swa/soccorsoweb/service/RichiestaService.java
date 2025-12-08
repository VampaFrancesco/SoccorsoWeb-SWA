package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.RichiestaSoccorsoMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import it.univaq.swa.soccorsoweb.repository.RichiestaSoccorsoRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RichiestaService {

    private final RichiestaSoccorsoMapper richiestaSoccorsoMapper;
    private final RichiestaSoccorsoRepository richiestaSoccorsoRepository;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public RichiestaService(RichiestaSoccorsoMapper richiestaSoccorsoMapper,
                            RichiestaSoccorsoRepository richiestaSoccorsoRepository,
                            EmailService emailService) {
        this.richiestaSoccorsoMapper = richiestaSoccorsoMapper;
        this.richiestaSoccorsoRepository = richiestaSoccorsoRepository;
        this.emailService = emailService;
    }

    public RichiestaSoccorsoResponse nuovaRichiesta(RichiestaSoccorsoRequest richiestaSoccorsoRequest,
                                                    HttpServletRequest request) throws MessagingException {
        log.info("Creazione nuova richiesta di soccorso da: {}", richiestaSoccorsoRequest.getNomeSegnalante());

        RichiestaSoccorso richiesta = richiestaSoccorsoMapper.toEntity(richiestaSoccorsoRequest);
        richiesta.setIpOrigine(getClientIp(request));
        richiesta.setTokenConvalida(UUID.randomUUID().toString());
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.INVIATA);  // ✅ Esplicito

        RichiestaSoccorso richiestaSalvata = richiestaSoccorsoRepository.save(richiesta);

        // ✅ Link corretto con ID e token
        String linkConvalida = baseUrl + "/swa/open/richiesta/" +
                richiestaSalvata.getId() +
                "/convalida/" +
                richiestaSalvata.getTokenConvalida();

        log.info("Link di convalida: {}", linkConvalida);

        emailService.inviaEmailConvalida(
                richiestaSalvata.getEmailSegnalante(),
                richiestaSalvata.getNomeSegnalante(),
                linkConvalida
        );

        log.info("Richiesta di soccorso creata con ID: {}", richiestaSalvata.getId());
        return richiestaSoccorsoMapper.toResponse(richiestaSalvata);
    }

    // ✅ Lancia eccezioni invece di ritornare boolean
    public void convalidaRichiesta(Long id, String token) {
        log.info("Tentativo di convalida richiesta ID: {} con token: {}", id, token);

        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));

        // ✅ Verifica stato
        if (richiesta.getStato() != RichiestaSoccorso.StatoRichiesta.INVIATA) {
            throw new IllegalStateException("Richiesta già convalidata o in lavorazione. Stato attuale: " + richiesta.getStato());
        }

        // ✅ Verifica token
        if (!richiesta.getTokenConvalida().equals(token)) {
            throw new IllegalArgumentException("Token di convalida non valido");
        }

        // ✅ Aggiorna stato
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.CONVALIDATA);
        richiesta.setConvalidataAt(LocalDateTime.now());

        richiestaSoccorsoRepository.save(richiesta);  // save() è sufficiente

        log.info("✅ Richiesta ID: {} convalidata con successo", id);
    }

    public List<RichiestaSoccorsoResponse> richiesteFiltrate(@NotNull
                                                             String stato) {
        List<RichiestaSoccorso> list;
        RichiestaSoccorso.StatoRichiesta statoEnum = RichiestaSoccorso.StatoRichiesta.valueOf(stato.toUpperCase());
            if (stato != null && !stato.isEmpty()) {
                return richiestaSoccorsoMapper.toResponseList(list = richiestaSoccorsoRepository.findAllByStato(statoEnum));
            }
            return null;
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

    public List<RichiestaSoccorsoResponse> richiesteValutateNegative() {

        List<RichiestaSoccorso> list;
        return richiestaSoccorsoMapper.toResponseList(list = richiestaSoccorsoRepository.findAllByLivelloSuccesso());


    }

    @Transactional
    public RichiestaSoccorsoResponse chiudiSoccorso(Long idSoccorso, String stato) throws MessagingException {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(idSoccorso)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + idSoccorso));

        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.valueOf(stato.toUpperCase()));
        richiesta.setUpdatedAt(LocalDateTime.now());
        emailService.inviaEmailChiusuraSoccorso(
                richiesta.getEmailSegnalante(),
                richiesta.getNomeSegnalante(),
                richiesta.getStato().toString(),
                String.valueOf(System.currentTimeMillis()),
                richiesta.getId()

        );
        return richiestaSoccorsoMapper.toResponse(richiestaSoccorsoRepository.save(richiesta));
    }



}