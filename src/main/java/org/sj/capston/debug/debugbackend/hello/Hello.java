package org.sj.capston.debug.debugbackend.hello;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String description;

    @Builder
    public Hello(
            @NonNull String name,
            @NonNull String description) {
        this.name = name;
        this.description = description;
    }
}
