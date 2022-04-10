package org.sj.capston.debug.debugbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DiseaseType {
    REDBEAN_MILDEW("팥-흰가루병"),
    REDBEAN_LEAF_BLIGHT("팥-세균잎마름병"),
    REDBEAN_RHIZOPUS("팥-거미줄곰팡이"),
    SESAME_BACTERIAL_LEAF_SPOT("참깨-세균성점무늬병"),
    SESAME_FUSARIUM_OXYSPORUM("참깨-시들음병"),
    SESAME_LEAF_BLIGHT("참깨-잎마름병"),
    SESAME_MILDEW("참깨-흰가루병");

    private final String name;
}
