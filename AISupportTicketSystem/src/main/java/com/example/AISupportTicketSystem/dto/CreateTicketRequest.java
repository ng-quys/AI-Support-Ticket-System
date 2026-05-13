package com.example.AISupportTicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {

    @Schema(description = "Short ticket title", example = "Payment failed during checkout")
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Schema(description = "Detailed ticket description", example = "My payment failed but money was deducted from my account.")
    @NotBlank(message = "Description must not be blank")
    private String description;
}
