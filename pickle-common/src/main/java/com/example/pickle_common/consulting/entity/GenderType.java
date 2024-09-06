package com.example.pickle_common.consulting.entity;

public enum GenderType {
    MALE(0), FEMALE(1);

    private final int value;

    private GenderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GenderType fromValue(int value) {
        for (GenderType type : GenderType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for GenderType: " + value);
    }
}
