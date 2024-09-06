package com.example.pickle_common.consulting.entity;

public enum AnswerType {
    OPTION_ZERO(0),
    OPTION_ONE(1),
    OPTION_TWO(2),
    OPTION_THREE(3);

    private final int value;

    AnswerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AnswerType fromValue(int value) {
        for (AnswerType type : AnswerType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for AnswerType: " + value);
    }
}
