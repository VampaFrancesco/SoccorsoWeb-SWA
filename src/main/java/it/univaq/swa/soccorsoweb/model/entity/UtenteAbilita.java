package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "utenti_abilita", indexes = {
        @Index(name = "idx_abilita_id", columnList = "abilita_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteAbilita {

    @EmbeddedId
    private UtenteAbilitaId id;

    @ManyToOne
    @MapsId("utenteId")
    @JoinColumn(name = "utente_id")
    private User utente;

    @ManyToOne
    @MapsId("abilitaId")
    @JoinColumn(name = "abilita_id")
    private Abilita abilita;

    @Column(length = 50)
    private String livello;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UtenteAbilitaId implements Serializable {
        @Column(name = "utente_id")
        private Long utenteId;

        @Column(name = "abilita_id")
        private Long abilitaId;
    }
}
