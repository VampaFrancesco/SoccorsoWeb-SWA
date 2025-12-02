package it.univaq.swa.soccorsoweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "abilita")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abilita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    // Relazione One-to-Many con UserAbilita
    @OneToMany(mappedBy = "abilita", cascade = CascadeType.ALL)
    private Set<UserAbilita> userAbilita = new HashSet<>();
}
