package org.sj.capston.debug.debugbackend.entity;

import lombok.*;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class BBox {

    private int xtl;

    private int ytl;

    private int xbr;

    private int ybr;
}
