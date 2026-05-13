package com.example.AISupportTicketSystem.service.impl;

import com.example.AISupportTicketSystem.dto.AiTicketAnalysisResult;
import com.example.AISupportTicketSystem.dto.CreateTicketRequest;
import com.example.AISupportTicketSystem.dto.TicketResponse;
import com.example.AISupportTicketSystem.entity.ClassificationSource;
import com.example.AISupportTicketSystem.entity.Ticket;
import com.example.AISupportTicketSystem.entity.TicketStatus;
import com.example.AISupportTicketSystem.exception.BadRequestException;
import com.example.AISupportTicketSystem.exception.ResourceNotFoundException;
import com.example.AISupportTicketSystem.repository.TicketRepository;
import com.example.AISupportTicketSystem.service.GeminiClassificationService;
import com.example.AISupportTicketSystem.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final GeminiClassificationService geminiClassificationService;

    @Override
    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
        String normalizedTitle = request.getTitle().trim();
        String normalizedDescription = request.getDescription().trim();

        AiTicketAnalysisResult analysisResult = geminiClassificationService.analyzeTicket(normalizedTitle, normalizedDescription);

        Ticket ticket = new Ticket();
        ticket.setTitle(normalizedTitle);
        ticket.setDescription(normalizedDescription);
        ticket.setCategory(analysisResult.getCategory());
        ticket.setPriority(analysisResult.getPriority());
        ticket.setSuggestedReply(analysisResult.getSuggestedReply());
        ticket.setClassificationSource(analysisResult.getClassificationSource());
        ticket.setStatus(TicketStatus.OPEN);

        Ticket savedTicket = ticketRepository.save(ticket);
        return toResponse(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TicketResponse updateTicketStatus(Long id, String status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        TicketStatus newStatus;
        try {
            newStatus = TicketStatus.fromInput(status);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(exception.getMessage());
        }

        ticket.setStatus(newStatus);
        Ticket updatedTicket = ticketRepository.saveAndFlush(ticket);
        return toResponse(updatedTicket);
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        ticketRepository.delete(ticket);
        ticketRepository.flush();
    }

    private TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .category(ticket.getCategory().getApiLabel())
                .priority(ticket.getPriority().getLabel())
                .suggestedReply(ticket.getSuggestedReply())
                .status(ticket.getStatus().getLabel())
                .classificationSource(resolveClassificationSource(ticket))
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }

    private String resolveClassificationSource(Ticket ticket) {
        ClassificationSource classificationSource = ticket.getClassificationSource();
        return classificationSource != null ? classificationSource.name() : ClassificationSource.UNKNOWN.name();
    }
}
