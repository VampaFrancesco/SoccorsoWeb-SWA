package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.RichiestaSoccorso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaSoccorsoRepository extends JpaRepository<RichiestaSoccorso, Long> {

    long countByStato(RichiestaSoccorso.StatoRichiesta statoRichiesta);
}
