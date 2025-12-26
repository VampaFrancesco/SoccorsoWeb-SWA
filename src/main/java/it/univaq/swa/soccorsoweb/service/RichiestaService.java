package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.RichiestaSoccorsoMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.RichiestaSoccorsoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MissioneResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

        RichiestaSoccorso richiesta = richiestaSoccorsoMapper.toEntity(richiestaSoccorsoRequest);
        richiesta.setIpOrigine(getClientIp(request));
        richiesta.setTokenConvalida(UUID.randomUUID().toString());
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.INVIATA);  // ✅ Esplicito

        RichiestaSoccorso richiestaSalvata = richiestaSoccorsoRepository.save(richiesta);

        // ✅ Link corretto con ID e token
        String linkConvalida = baseUrl + "/swa/open/richieste/convalida?token_convalida=" + richiestaSalvata.getTokenConvalida();

        emailService.inviaEmailConvalida(
                richiestaSalvata.getEmailSegnalante(),
                richiestaSalvata.getNomeSegnalante(),
                linkConvalida
        );

        return richiestaSoccorsoMapper.toResponse(richiestaSalvata);
    }


  public void convalidaRichiesta(String token) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findByTokenConvalida(token);

        if(richiesta == null) {
            throw new EntityNotFoundException("Token di convalida non valido.");
        }

        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.CONVALIDATA);
        richiesta.setUpdatedAt(LocalDateTime.now());
        richiestaSoccorsoRepository.save(richiesta);

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
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));
        return richiestaSoccorsoMapper.toResponse(richiesta);
    }

    public RichiestaSoccorsoResponse annullaRichiesta(Long id) {
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Richiesta non trovata con ID: " + id));
        richiesta.setStato(RichiestaSoccorso.StatoRichiesta.ANNULLATA);
        richiesta.setUpdatedAt(LocalDateTime.now());
        RichiestaSoccorso richiestaAggiornata = richiestaSoccorsoRepository.save(richiesta);
        return richiestaSoccorsoMapper.toResponse(richiestaAggiornata);
    }
}