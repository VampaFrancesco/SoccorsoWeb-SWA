package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "missione_mezzo")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissioneMezzo {

    @EmbeddedId
    private MissioneMezzoId id;

    @ManyToOne
    @MapsId("missioneId")
    @JoinColumn(name = "missione_id")
    private Missione missione;

    @ManyToOne
    @MapsId("mezzoId")
    @JoinColumn(name = "mezzo_id")
    private Mezzo mezzo;

    @Column(name = "assegnato_at")
    private LocalDateTime assegnatoAt;

    // Classe per la chiave composta
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MissioneMezzoId implements Serializable {
        @Column(name = "missione_id")
        private Long missioneId;

        @Column(name = "mezzo_id")
        private Long mezzoId;
    }
}

