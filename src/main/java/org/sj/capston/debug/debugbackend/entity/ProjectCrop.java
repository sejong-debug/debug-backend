package org.sj.capston.debug.debugbackend.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ProjectCrop extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(STRING)
    @Column(nullable = false)
    private CropType cropType;
}
