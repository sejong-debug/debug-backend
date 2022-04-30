package org.sj.capston.debug.debugbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

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

    @Column(nullable = false)
    private double disease1;

    @Column(nullable = false)
    private double disease2;

    @Column(nullable = false)
    private double disease3;

    @Column(nullable = false)
    private double disease4;

    @Column(nullable = false)
    private double disease5;

    @Column(nullable = false)
    private double disease6;

    @Column(nullable = false)
    private double disease7;

    @Column(nullable = false)
    private double disease8;

    @Column(nullable = false)
    private double disease9;

    @Column(nullable = false)
    private double disease10;

    @Column(nullable = false)
    private double disease11;

    @Column(nullable = false)
    private double disease12;

    @Column(nullable = false)
    private double disease13;

    @Column(nullable = false)
    private double disease14;

    @Column(nullable = false)
    private double disease15;

    @Column(nullable = false)
    private double disease16;

    @Column(nullable = false)
    private double disease17;

    @Column(nullable = false)
    private double disease18;

    @Column(nullable = false)
    private double disease19;

    @Column(nullable = false)
    private double disease20;

    @Column(nullable = false)
    private double disease21;

    @Column(nullable = false)
    private double disease22;

    @Column(nullable = false)
    private double disease23;

    @Column(nullable = false)
    private double disease24;

    @Column(nullable = false)
    private double disease25;

    @Column(nullable = false)
    private double disease26;

    @Column(nullable = false)
    private double disease27;

    @Column(nullable = false)
    private double disease28;

    @Column(nullable = false)
    private double disease29;

    @Column(nullable = false)
    private double disease30;

    @Column(nullable = false)
    private double disease31;

    @Column(nullable = false)
    private double disease32;

    @Column(nullable = false)
    private double disease33;

    @Column(nullable = false)
    private double disease34;

    @Column(nullable = false)
    private double disease35;

    @Column(nullable = false)
    private double disease36;

    @Column(nullable = false)
    private double disease37;

    @Column(nullable = false)
    private double disease38;

    @Column(nullable = false)
    private double disease39;

    @Column(nullable = false)
    private double disease40;

    @Column(nullable = false)
    private double disease41;

    @Column(nullable = false)
    private double disease42;

    @Column(nullable = false)
    private double disease43;

    @Column(nullable = false)
    private double disease44;

    @Column(nullable = false)
    private double disease45;

    @Column(nullable = false)
    private double disease46;

    @Column(nullable = false)
    private double disease47;

    @Column(nullable = false)
    private double disease48;

    @Column(nullable = false)
    private double disease49;

    @Column(nullable = false)
    private double disease50;

    @Column(nullable = false)
    private double disease51;

    @Column(nullable = false)
    private double disease52;

    @Column(nullable = false)
    private double disease53;

    @Column(nullable = false)
    private double disease54;

    @Column(nullable = false)
    private double disease55;

    @Column(nullable = false)
    private double disease56;

    @Column(nullable = false)
    private double disease57;

    @Column(nullable = false)
    private double disease58;

    @Column(nullable = false)
    private double disease59;

    @Column(nullable = false)
    private double disease60;

    @Column(nullable = false)
    private double disease61;

    @Column(nullable = false)
    private double disease62;

    @Column(nullable = false)
    private double disease63;

    @Column(nullable = false)
    private double disease64;

    @Column(nullable = false)
    private double disease65;

    @Column(nullable = false)
    private double disease66;

    @Column(nullable = false)
    private double disease67;

    @Column(nullable = false)
    private double disease68;

    @Column(nullable = false)
    private double disease69;

    @Column(nullable = false)
    private double disease70;

    @Column(nullable = false)
    private double disease71;

    @Column(nullable = false)
    private double disease72;

    @Column(nullable = false)
    private double disease73;

    @Column(nullable = false)
    private double disease74;

    @Column(nullable = false)
    private double disease75;

    static public Map<String, String> DISEASES = new HashMap<String, String>(){{
            put("disease1", "팥_흰가루병");
            put("disease2", "팥_세균잎마름병");
            put("disease3", "팥_Rhizopus");
            put("disease4", "참깨_세균성점무늬병");
            put("disease5", "참깨_시들음병");
            put("disease6", "참깨_잎마름병");
            put("disease7", "참깨_흰가루병");
            put("disease8", "콩_노균병");
            put("disease9", "콩_들불병");
            put("disease10", "콩_불마름병");
            put("disease11", "콩_병징");
            put("disease12", "가지_잎곰팡이병");
            put("disease13", "가지_흰가루병");
            put("disease14", "고추_마일드모틀바이러스병");
            put("disease15", "고추_점무늬병");
            put("disease16", "단호박_점무늬병");
            put("disease17", "단호박_흰가루병");
            put("disease18", "딸기_잿빛곰팡이병");
            put("disease19", "딸기_흰가루병");
            put("disease20", "상추_균핵병");
            put("disease21", "상추_노균병");
            put("disease22", "수박_탄저병");
            put("disease23", "수박_흰가루병");
            put("disease24", "애호박_점무늬병");
            put("disease25", "오이_녹반모자이크바이러스");
            put("disease26", "쥬키니호박_오이녹반모자이크바이러스");
            put("disease27", "참외_노균병");
            put("disease28", "참외_흰가루병");
            put("disease29", "토마토_잎곰팡이병");
            put("disease30", "토마토_황화잎말이바이러스병");
            put("disease31", "포도_노균병");
            put("disease32", "검거세미밤나방");
            put("disease33", "꽃노랑총채벌레");
            put("disease34", "담배가루이");
            put("disease35", "담배거세미나방");
            put("disease36", "담배나방");
            put("disease37", "도둑나방");
            put("disease38", "먹노린재");
            put("disease39", "목화바둑명나방");
            put("disease40", "무잎벌");
            put("disease41", "배추좀나방");
            put("disease42", "배추흰나비");
            put("disease43", "벼룩잎벌레");
            put("disease44", "복숭아혹진딧물");
            put("disease45", "비단노린재");
            put("disease46", "썩덩나무노린재");
            put("disease47", "열대거세미나방");
            put("disease48", "큰28점박이무당벌레");
            put("disease49", "톱다리개미허리노린재");
            put("disease50", "파밤나방");
            put("disease51", "고추_탄저병");
            put("disease52", "고추_흰가루병");
            put("disease53", "무_검은무늬병");
            put("disease54", "무_노균병");
            put("disease55", "배추_검은썩음병");
            put("disease56", "배추_노균병");
            put("disease57", "애호박_노균병");
            put("disease58", "애호박_흰가루병");
            put("disease59", "양배추_균핵병");
            put("disease60", "양배추_무름병");
            put("disease61", "오이_노균병");
            put("disease62", "오이_흰가루병");
            put("disease63", "콩_점무늬병");
            put("disease64", "토마토_잎마름병");
            put("disease65", "파_검은무늬병");
            put("disease66", "파_노균병");
            put("disease67", "파_녹병");
            put("disease68", "호박_노균병");
            put("disease69", "호박_흰가루병");
            put("disease70", "배과수_화상병");
            put("disease71", "사과_갈색무늬병");
            put("disease72", "사과_과수화상병");
            put("disease73", "사과_부란병");
            put("disease74", "사과_점무늬낙엽병");
            put("disease75", "사과_탄저병");
    }};
}
