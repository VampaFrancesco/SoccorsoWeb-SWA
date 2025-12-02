package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "user_patente")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPatente {

    @EmbeddedId
    private UserPatenteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("patenteId")
    @JoinColumn(name = "patente_id")
    private Patente patente;

    @Column(name = "conseguita_il")
    private LocalDate conseguitaIl;

    // Classe per la chiave composta
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserPatenteId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "patente_id")
        private Long patenteId;
    }
}

