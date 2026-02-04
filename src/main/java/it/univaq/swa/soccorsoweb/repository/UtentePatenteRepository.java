package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.UtentePatente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtentePatenteRepository extends JpaRepository<UtentePatente, UtentePatente.UtentePatenteId> {
}
