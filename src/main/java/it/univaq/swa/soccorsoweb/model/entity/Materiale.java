package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "materiale")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materiale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(length = 50)
    private String tipo;

    @Column(nullable = false)
    private Integer quantita = 1;

    @Column(nullable = false)
    private Boolean disponibile = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relazione One-to-Many con MissioneMateriale
    @OneToMany(mappedBy = "materiale", cascade = CascadeType.ALL)
    private Set<MissioneMateriale> missioneMateriali = new HashSet<>();
}

