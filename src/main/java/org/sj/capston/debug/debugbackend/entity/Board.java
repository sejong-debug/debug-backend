package org.sj.capston.debug.debugbackend.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "board_image_id", nullable = false)
    private BoardImage boardImage;
}
