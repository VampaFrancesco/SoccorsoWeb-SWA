package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mezzo")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(length = 50)
    private String tipo;

    @Column(length = 20)
    private String targa;

    @Column(nullable = false)
    private Boolean disponibile = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relazione One-to-Many con MissioneMezzo
    @OneToMany(mappedBy = "mezzo", cascade = CascadeType.ALL)
    private Set<MissioneMezzo> missioneMezzi = new HashSet<>();
}
