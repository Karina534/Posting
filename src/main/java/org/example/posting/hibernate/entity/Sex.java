package org.example.posting.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Sex {
    MALE,
    FEMALE,
    OTHER;

    @JsonCreator
    public static Sex fromString(String value){
        return value == null ? Sex.OTHER : Sex.valueOf(value.toUpperCase());
    }
}
