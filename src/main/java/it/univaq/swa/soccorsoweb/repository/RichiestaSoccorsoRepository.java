package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaSoccorsoRepository extends JpaRepository<RichiestaSoccorso, Long> {

    /**
     * Query paginata per stato
     * Spring Data JPA genera automaticamente:
     * SELECT * FROM richiesta_soccorso WHERE stato = :stato LIMIT :size OFFSET :offset
     */
    Page<RichiestaSoccorso> findByStato(RichiestaSoccorso.StatoRichiesta stato, Pageable pageable);

    RichiestaSoccorso findByTokenConvalida(String tokenConvalida);

    @Query("SELECT r FROM RichiestaSoccorso r WHERE r.livelloSuccesso IS NOT NULL AND r.livelloSuccesso < 5 AND r.stato = 'CHIUSA'")  //possiamo valutare solo le richieste chiuse
    List<RichiestaSoccorso> findAllByLivelloSuccessoAndStato();
}
