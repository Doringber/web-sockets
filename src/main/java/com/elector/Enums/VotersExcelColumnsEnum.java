package com.elector.Enums;

public enum VotersExcelColumnsEnum {
    id(1),
    first_name(2),
    last_name(3),
    phone(4),
    birth_date(5),
    address(6);

    private int value = 0;

    VotersExcelColumnsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VotersExcelColumnsEnum getEnum(Integer value) {
        if (value != null) {
            for (VotersExcelColumnsEnum l : VotersExcelColumnsEnum.values()) {
                if (l.value == value) {
                    return l;
                }
            }
            throw new IllegalArgumentException("VotersExcelColumnsEnum not found.");
        } else {
            return null;
        }
    }
}
