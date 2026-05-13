package com.example.AISupportTicketSystem.entity;

public enum TicketPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String label;

    TicketPriority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TicketPriority fromLabel(String value) {
        if (value == null || value.isBlank()) {
            return MEDIUM;
        }
        for (TicketPriority priority : values()) {
            if (priority.label.equalsIgnoreCase(value.trim())) {
                return priority;
            }
        }
        return MEDIUM;
    }
}
