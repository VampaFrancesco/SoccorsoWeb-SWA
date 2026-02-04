package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.SquadraOperatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadraOperatoreRepository
        extends JpaRepository<SquadraOperatore, SquadraOperatore.SquadraOperatoreId> {
}
