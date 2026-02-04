package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.MissioneMezzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissioneMezzoRepository extends JpaRepository<MissioneMezzo, MissioneMezzo.MissioneMezzoId> {
}
