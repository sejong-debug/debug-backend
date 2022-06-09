package org.sj.capstone.debug.debugbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Issue extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "board_image_id", nullable = false)
    private BoardImage boardImage;

    @ElementCollection
    @CollectionTable(name = "issue_probability",
            joinColumns = @JoinColumn(name = "issue_id", nullable = false)
    )
    @MapKeyColumn(name = "name", length = 45)
    @Column(name = "probability", nullable = false)
    @Builder.Default
    private Map<String, Double> nameToProbs = new TreeMap<>();
}
