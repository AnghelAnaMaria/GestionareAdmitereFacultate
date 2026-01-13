package org.example.model.entities;

import java.util.Arrays;

public enum ExamType {
    WRITTEN,
    ORAL,
    ONLINE;

    public static ExamType getValueByString(String examType) {
        return Arrays.stream(ExamType.values())
                .filter(e -> e.name().equals(examType))
                .findAny()
                .orElse(null);
    }
}
