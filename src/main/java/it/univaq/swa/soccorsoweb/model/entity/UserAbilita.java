package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_abilita")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAbilita {

    @EmbeddedId
    private UserAbilitaId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("abilitaId")
    @JoinColumn(name = "abilita_id")
    private Abilita abilita;

    @Column(nullable = false, length = 50)
    private String livello;

    // Classe per la chiave composta
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserAbilitaId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "abilita_id")
        private Long abilitaId;
    }
}

