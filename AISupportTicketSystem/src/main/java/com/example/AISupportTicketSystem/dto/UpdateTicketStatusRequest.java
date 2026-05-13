package com.example.AISupportTicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTicketStatusRequest {

    @Schema(description = "Standardized ticket status enum", allowableValues = {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED"}, example = "IN_PROGRESS")
    @NotBlank(message = "status must not be blank")
    private String status;
}
