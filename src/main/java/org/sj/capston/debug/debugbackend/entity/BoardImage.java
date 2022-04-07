package org.sj.capston.debug.debugbackend.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BoardImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false, length = 25)
    private String ext;
}
