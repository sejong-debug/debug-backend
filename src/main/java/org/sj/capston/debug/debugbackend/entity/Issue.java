package org.sj.capston.debug.debugbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Issue {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "image_info_id", nullable = false)
    private BoardImage boardImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private DiseaseType diseaseType;

    @Embedded
    private BBox bBox;
}
