package org.sj.capston.debug.debugbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 45)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private CropType cropType;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private boolean completed = false;
}
