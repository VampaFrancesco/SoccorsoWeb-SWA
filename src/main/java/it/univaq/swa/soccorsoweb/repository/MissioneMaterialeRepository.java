package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.MissioneMateriale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissioneMaterialeRepository
        extends JpaRepository<MissioneMateriale, MissioneMateriale.MissioneMaterialeId> {
}
