package com.example.AISupportTicketSystem.entity;

public enum TicketCategory {
    AUTHENTICATION_ISSUE("Authentication Issue"),
    PAYMENT_ISSUE("Payment Issue"),
    ACCOUNT_ISSUE("Account Issue"),
    TECHNICAL_BUG("Technical Bug"),
    GENERAL_INQUIRY("General Inquiry"),
    OTHER("Other");

    private final String label;

    TicketCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TicketCategory fromLabel(String value) {
        if (value == null || value.isBlank()) {
            return OTHER;
        }

        String normalized = value.trim().replace('_', ' ');
        for (TicketCategory category : values()) {
            if (category.name().equalsIgnoreCase(value.trim())) {
                return category;
            }
            if (category.label.equalsIgnoreCase(normalized)) {
                return category;
            }
        }

        if ("Login Issue".equalsIgnoreCase(normalized)
                || "Authentication Issue".equalsIgnoreCase(normalized)) {
            return AUTHENTICATION_ISSUE;
        }

        return OTHER;
    }

    public String getApiLabel() {
        return label;
    }
}
