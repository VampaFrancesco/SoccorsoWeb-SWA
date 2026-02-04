package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.Abilita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbilitaRepository extends JpaRepository<Abilita, Long> {
}
