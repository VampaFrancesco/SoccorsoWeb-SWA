package it.univaq.swa.soccorsoweb.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patente", indexes = {
        @Index(name = "idx_tipo", columnList = "tipo")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    // Relazione One-to-Many con UserPatente
    @OneToMany(mappedBy = "patente", cascade = CascadeType.ALL)
    private Set<UserPatente> userPatenti = new HashSet<>();
}
