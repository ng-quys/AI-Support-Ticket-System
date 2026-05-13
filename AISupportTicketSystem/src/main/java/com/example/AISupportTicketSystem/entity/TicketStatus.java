package com.example.AISupportTicketSystem.entity;

public enum TicketStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String label;

    TicketStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TicketStatus fromInput(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("status must not be blank");
        }

        String normalized = value.trim().replace(' ', '_').replace('-', '_');
        for (TicketStatus status : values()) {
            if (status.name().equalsIgnoreCase(normalized)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid status. Allowed values: OPEN, IN_PROGRESS, RESOLVED, CLOSED");
    }
}
