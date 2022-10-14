package com.api.fascinareventos.models.enums;

public enum EventStatus {
    A_REALIZAR(1),
    REALIZADO(2),
    CANCELADO(3);

    private final int numVal;

    EventStatus(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
