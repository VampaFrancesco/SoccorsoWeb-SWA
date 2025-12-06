package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.RichiestaSoccorsoMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import it.univaq.swa.soccorsoweb.repository.RichiestaSoccorsoRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public long contaRichiesteConvalidate() {
        return richiestaSoccorsoRepository.countByStato(RichiestaSoccorso.StatoRichiesta.CONVALIDATA);
    }
}
