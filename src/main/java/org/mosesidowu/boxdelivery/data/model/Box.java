package org.mosesidowu.boxdelivery.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String txref;

    @Column(nullable = false)
    private int weightLimit;

    @Column(nullable = false)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Item> items;



    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }
}