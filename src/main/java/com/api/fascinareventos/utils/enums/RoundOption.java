package com.api.fascinareventos.utils.enums;

import java.util.Arrays;

public enum RoundOption {
    DISABLE(0),
    ROUND_1(1),
    ROUND_5(5),
    ROUND_10(10);

    private final int value;

    RoundOption(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoundOption valueOf(int value) {
        return Arrays.stream(RoundOption.values())
                .filter(v -> v.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid RoundOption value"));
    }

}
