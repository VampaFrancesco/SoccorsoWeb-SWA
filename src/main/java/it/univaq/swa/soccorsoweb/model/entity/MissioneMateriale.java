package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "missione_materiale")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissioneMateriale {

    @EmbeddedId
    private MissioneMaterialeId id;

    @ManyToOne
    @MapsId("missioneId")
    @JoinColumn(name = "missione_id")
    private Missione missione;

    @ManyToOne
    @MapsId("materialeId")
    @JoinColumn(name = "materiale_id")
    private Materiale materiale;

    @Column(name = "quantita_usata")
    private Integer quantitaUsata;

    @Column(name = "assegnato_at")
    private LocalDateTime assegnatoAt;

    // Classe per la chiave composta
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MissioneMaterialeId implements Serializable {
        @Column(name = "missione_id")
        private Long missioneId;

        @Column(name = "materiale_id")
        private Long materialeId;
    }
}

