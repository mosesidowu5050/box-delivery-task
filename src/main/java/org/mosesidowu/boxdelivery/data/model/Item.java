package org.mosesidowu.boxdelivery.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Pattern(regexp = "^[A-Za-z0-9_-]+$")
        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private int weight;

        @Pattern(regexp = "^[A-Z0-9_]+$")
        @Column(nullable = false, unique = true)
        private String code;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "box_id")
        private Box box;

}
