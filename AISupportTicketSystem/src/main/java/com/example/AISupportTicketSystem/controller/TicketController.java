package com.example.AISupportTicketSystem.controller;

import com.example.AISupportTicketSystem.dto.CreateTicketRequest;
import com.example.AISupportTicketSystem.dto.TicketResponse;
import com.example.AISupportTicketSystem.dto.UpdateTicketStatusRequest;
import com.example.AISupportTicketSystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Get all tickets", description = "Returns the ticket list with standardized category, status, and classification source fields.")
    @ApiResponse(responseCode = "200", description = "Ticket list retrieved successfully")
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @Operation(summary = "Create a ticket", description = "Creates a new support ticket and classifies it using Gemini or fallback logic.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request));
    }

    @Operation(summary = "Update ticket status", description = "Updates ticket status using standardized enum values: OPEN, IN_PROGRESS, RESOLVED, CLOSED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status input"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(@PathVariable Long id,
                                                             @Valid @RequestBody UpdateTicketStatusRequest request) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, request.getStatus()));
    }

    @Operation(summary = "Delete a ticket", description = "Deletes a ticket by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
