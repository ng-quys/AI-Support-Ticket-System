package com.example.AISupportTicketSystem.service;

import com.example.AISupportTicketSystem.dto.CreateTicketRequest;
import com.example.AISupportTicketSystem.dto.TicketResponse;
import java.util.List;

public interface TicketService {
    TicketResponse createTicket(CreateTicketRequest request);

    List<TicketResponse> getAllTickets();

    TicketResponse updateTicketStatus(Long id, String status);

    void deleteTicket(Long id);
}
