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
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final RichiestaService richiestaService;
    private final UserRepository userRepository;

    public List<MissioneResponse> missioniValutateNegative() {
        return missioneMapper.toResponseList(missioneRepository.findAllByLivelloSuccesso());
    }

    @Transactional
    public MissioneResponse modificaMissione(Long id, String stato) {
        //Cerca la missione
        Missione missione = missioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Missione non trovata con ID: " + id));
        Missione.StatoMissione nuovoStato;
        nuovoStato = Missione.StatoMissione.valueOf(stato.toUpperCase());
        missione.setStato(nuovoStato);
        missione.setUpdatedAt(LocalDateTime.now());

        //Se la missione viene chiusa o fallita, imposta anche la data di fine
        if (nuovoStato == Missione.StatoMissione.CHIUSA || nuovoStato == Missione.StatoMissione.FALLITA) {
            missione.setFineAt(LocalDateTime.now());
            //Aggiorna anche lo stato della richiesta
            RichiestaSoccorso richiesta = missione.getRichiesta();
            if (richiesta != null) {
                RichiestaSoccorso.StatoRichiesta statoRichiesta =
                        nuovoStato == Missione.StatoMissione.CHIUSA
                                ? RichiestaSoccorso.StatoRichiesta.CHIUSA
                                : RichiestaSoccorso.StatoRichiesta.IGNORATA;
                richiesta.setStato(statoRichiesta);
            }
        }


        Missione missioneSalvata = missioneRepository.save(missione);
        return missioneMapper.toResponse(missioneSalvata);
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

    @Transactional
    public MissioneResponse inserisciMissione(@Valid MissioneRequest missioneRequest) {

        //ottengo caposquadra
        User caposquadra = userRepository.findById(missioneRequest.getCaposquadraId()).orElseThrow();

        //ottengo richiesta
        RichiestaSoccorso richiesta = richiestaSoccorsoRepository.findById(missioneRequest.getRichiestaId()).orElseThrow();

        //creo missione
        Missione missione = missioneMapper.toEntity(missioneRequest);
        missione.setLatitudine(richiesta.getLatitudine());
        missione.setLongitudine(richiesta.getLongitudine());

        //setto caposquadra e richiesta
        missione.setCaposquadra(caposquadra);
        missione.setRichiesta(richiesta);

        //gestisco operatori
        if (missioneRequest.getOperatoriIds() != null && !missioneRequest.getOperatoriIds().isEmpty()) {
            Set<MissioneOperatore> missioneOperatori = new HashSet<>();
            List<User> operatori = userRepository.findAllById(missioneRequest.getOperatoriIds());
            for (User operatore : operatori) {
                MissioneOperatore missioneOperatore = new MissioneOperatore();
                missioneOperatore.setOperatore(operatore);
                missioneOperatore.setMissione(missione);
                MissioneOperatore.MissioneOperatoreId id = new MissioneOperatore.MissioneOperatoreId();
                id.setMissioneId(missione.getId());
                id.setOperatoreId(operatore.getId());
                missioneOperatore.setId(id);
                missioneOperatori.add(missioneOperatore);
            }
        }else{
            missione.setMissioneOperatori(new HashSet<>());
        }
            return missioneMapper.toResponse(missioneRepository.save(missione));
    }

    public MissioneResponse chiudiMissione(Long id) {

        Missione missione = missioneRepository.findById(id).orElseThrow();
        missione.setStato(Missione.StatoMissione.CHIUSA);
        richiestaService.modificaRichiesta(missione.getRichiesta().getId(), "CHIUSA");
        missione.setFineAt(LocalDateTime.now());
        Missione missioneSalvata = missioneRepository.save(missione);
        return missioneMapper.toResponse(missioneSalvata);
    }

    public MissioneResponse valutaMissione(Long id, Integer livelloSuccesso) {
        Missione missione = missioneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Missione non trovata con ID: " + id));
        missione.setLivelloSuccesso(livelloSuccesso);
        Missione missioneSalvata = missioneRepository.save(missione);
        return missioneMapper.toResponse(missioneSalvata);
    }

    public MissioneResponse dettagliMissione(Long id) {
        Missione missione = missioneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Missione non trovata con ID: " + id));
        return missioneMapper.toResponse(missione);
    }

    public List<MissioneResponse> missioniOperatore(Long id) {
        List<Missione> missioni = missioneRepository.findAllByOperatoreId(id);
        return missioneMapper.toResponseList(missioni);
    }
}
