package com.interswitch.first_api.models.enums;

public enum Department {
    ENGINEERING("Engineering"),
    MARKETING("Marketing"),
    FINANCE("Finance"),
    HR("Human Resources"),
    SALES("Sales"),
    OPERATIONS("Operations"),
    LEGAL("Legal"),
    IT("Information Technology");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}