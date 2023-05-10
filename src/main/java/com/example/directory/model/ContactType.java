package com.example.directory.model;

public enum ContactType {
    MOBITEL("Mobitel"),
    FIXNI_TELEFON("Fiksni telefon"),
    EMAIL("Email"),
    PAGER("Pager");


    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
