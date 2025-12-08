package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.Missione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissioneRepository extends JpaRepository<Missione, Long> {

    @Query("SELECT m FROM Missione m WHERE m.livelloSuccesso IS NOT NULL AND m.livelloSuccesso < 5")
    List<Missione> findAllByLivelloSuccesso();
}
