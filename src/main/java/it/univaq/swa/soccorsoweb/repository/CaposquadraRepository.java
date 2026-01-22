package it.univaq.swa.soccorsoweb.repository;

import it.univaq.swa.soccorsoweb.model.entity.Caposquadra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaposquadraRepository extends JpaRepository<Caposquadra, Long> {
    Optional<Caposquadra> findByUtenteId(Long utenteId);
}
