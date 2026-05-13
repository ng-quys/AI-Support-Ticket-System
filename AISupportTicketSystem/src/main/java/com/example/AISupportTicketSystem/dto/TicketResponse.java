package com.example.AISupportTicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketResponse {
    @Schema(example = "42")
    private Long id;

    @Schema(example = "Need account help")
    private String title;

    @Schema(example = "I cannot login to my account and need authentication support")
    private String description;

    @Schema(description = "Display label for standardized category enum", example = "Authentication Issue", allowableValues = {"Authentication Issue", "Payment Issue", "Account Issue", "Technical Bug", "General Inquiry", "Other"})
    private String category;

    @Schema(example = "High", allowableValues = {"Low", "Medium", "High"})
    private String priority;

    @Schema(example = "We understand you're having trouble logging in. Please try resetting your password via the 'Forgot Password' link on the login page. If the issue persists, please contact us again with your registered email address.")
    private String suggestedReply;

    @Schema(description = "Display label for standardized status enum", example = "In Progress", allowableValues = {"Open", "In Progress", "Resolved", "Closed"})
    private String status;

    @Schema(example = "GEMINI", allowableValues = {"GEMINI", "KEYWORD_FALLBACK", "DEFAULT_FALLBACK", "UNKNOWN"})
    private String classificationSource;

    @Schema(example = "2026-05-13T07:26:58")
    private LocalDateTime createdAt;

    @Schema(example = "2026-05-13T07:26:59")
    private LocalDateTime updatedAt;
}
