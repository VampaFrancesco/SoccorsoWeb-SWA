package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaSoccorsoRepository extends JpaRepository<RichiestaSoccorso, Long> {

    List<RichiestaSoccorso> findAllByStato(RichiestaSoccorso.StatoRichiesta stato);

    RichiestaSoccorso findByTokenConvalida(String tokenConvalida);

    @Query("SELECT r FROM RichiestaSoccorso r WHERE r.livelloSuccesso IS NOT NULL AND r.livelloSuccesso < 5 AND r.stato = 'CHIUSA'")  //possiamo valutare solo le richieste chiuse
    List<RichiestaSoccorso> findAllByLivelloSuccessoAndStato();
}
