package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "missione_operatori")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissioneOperatore {

    @EmbeddedId
    private MissioneOperatoreId id;

    @ManyToOne
    @MapsId("missioneId")
    @JoinColumn(name = "missione_id")
    private Missione missione;

    @ManyToOne
    @MapsId("operatoreId")
    @JoinColumn(name = "operatore_id")
    private User operatore;


    // Classe per la chiave composta
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MissioneOperatoreId implements Serializable {
        @Column(name = "missione_id")
        private Long missioneId;

        @Column(name = "operatore_id")
        private Long operatoreId;
    }
}

