package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.MissioneOperatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissioneOperatoreRepository
        extends JpaRepository<MissioneOperatore, MissioneOperatore.MissioneOperatoreId> {
}
