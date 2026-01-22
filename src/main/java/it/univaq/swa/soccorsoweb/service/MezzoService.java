package it.univaq.swa.soccorsoweb.service;

import it.univaq.swa.soccorsoweb.mapper.MezzoMapper;
import it.univaq.swa.soccorsoweb.model.dto.request.MezzoRequest;
import it.univaq.swa.soccorsoweb.model.dto.response.MezzoResponse;
import it.univaq.swa.soccorsoweb.model.entity.Mezzo;
import it.univaq.swa.soccorsoweb.repository.MezzoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MezzoService {

    private final MezzoRepository mezzoRepository;
    private final MezzoMapper mezzoMapper;

    public List<MezzoResponse> getAll() {
        return mezzoMapper.toResponseList(mezzoRepository.findAll());
    }

    public MezzoResponse getById(Long id) {
        Mezzo mezzo = mezzoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mezzo non trovato: " + id));
        return mezzoMapper.toResponse(mezzo);
    }

    @Transactional
    public MezzoResponse create(MezzoRequest request) {
        Mezzo mezzo = mezzoMapper.toEntity(request);
        mezzo = mezzoRepository.save(mezzo);
        return mezzoMapper.toResponse(mezzo);
    }

    @Transactional
    public void delete(Long id) {
        if (!mezzoRepository.existsById(id)) {
            throw new EntityNotFoundException("Mezzo non trovato: " + id);
        }
        mezzoRepository.deleteById(id);
    }
}
