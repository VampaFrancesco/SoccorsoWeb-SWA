package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "missione", indexes = {
        @Index(name = "idx_stato", columnList = "stato"),
        @Index(name = "idx_caposquadra", columnList = "caposquadra_id")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione One-to-One con RichiestaSoccorso
    @OneToOne
    @JoinColumn(name = "richiesta_id", nullable = false, unique = true)
    private RichiestaSoccorso richiesta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String obiettivo;

    private String posizione;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitudine;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitudine;

    // Relazione Many-to-One con User (caposquadra)
    @ManyToOne
    @JoinColumn(name = "caposquadra_id", nullable = false)
    private User caposquadra;

    @Column(name = "inizio_at")
    private LocalDateTime inizioAt = LocalDateTime.now();

    @Column(name = "fine_at")
    private LocalDateTime fineAt;

    @Column(name = "livello_successo")
    private Integer livelloSuccesso;

    @Column(name = "commenti_finali", columnDefinition = "TEXT")
    private String commentiFinali;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoMissione stato = StatoMissione.IN_CORSO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relazione One-to-Many con MissioneOperatore (entity di relazione)
    @OneToMany(mappedBy = "missione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MissioneOperatore> missioneOperatori = new HashSet<>();


    // Enum per lo stato
    public enum StatoMissione {
        IN_CORSO, CHIUSA, FALLITA, ANNULLATA
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.inizioAt = LocalDateTime.now();
        this.stato = StatoMissione.IN_CORSO;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
