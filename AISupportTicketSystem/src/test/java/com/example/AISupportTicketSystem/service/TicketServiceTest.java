package com.example.AISupportTicketSystem.service;

import com.example.AISupportTicketSystem.dto.AiTicketAnalysisResult;
import com.example.AISupportTicketSystem.dto.CreateTicketRequest;
import com.example.AISupportTicketSystem.dto.TicketResponse;
import com.example.AISupportTicketSystem.entity.ClassificationSource;
import com.example.AISupportTicketSystem.entity.Ticket;
import com.example.AISupportTicketSystem.entity.TicketCategory;
import com.example.AISupportTicketSystem.entity.TicketPriority;
import com.example.AISupportTicketSystem.entity.TicketStatus;
import com.example.AISupportTicketSystem.exception.ResourceNotFoundException;
import com.example.AISupportTicketSystem.repository.TicketRepository;
import com.example.AISupportTicketSystem.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private GeminiClassificationService geminiClassificationService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void createTicket_shouldCreateTicketSuccessfully() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle(" Cannot login ");
        request.setDescription(" User cannot login to system ");

        AiTicketAnalysisResult analysisResult = AiTicketAnalysisResult.builder()
                .category(TicketCategory.ACCOUNT_ISSUE)
                .priority(TicketPriority.HIGH)
                .suggestedReply("Please reset your password.")
                .classificationSource(ClassificationSource.GEMINI)
                .build();

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setTitle("Cannot login");
        savedTicket.setDescription("User cannot login to system");
        savedTicket.setCategory(TicketCategory.ACCOUNT_ISSUE);
        savedTicket.setPriority(TicketPriority.HIGH);
        savedTicket.setSuggestedReply("Please reset your password.");
        savedTicket.setClassificationSource(ClassificationSource.GEMINI);
        savedTicket.setStatus(TicketStatus.OPEN);

        when(geminiClassificationService.analyzeTicket("Cannot login", "User cannot login to system"))
                .thenReturn(analysisResult);

        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        TicketResponse result = ticketService.createTicket(request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Cannot login");
        assertThat(result.getStatus()).isEqualTo(TicketStatus.OPEN.getLabel());

        verify(geminiClassificationService, times(1))
                .analyzeTicket("Cannot login", "User cannot login to system");

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void deleteTicket_shouldThrowException_whenTicketNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.deleteTicket(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ticket not found with id: 99");

        verify(ticketRepository, never()).delete(any(Ticket.class));
    }
    @Test
    void updateTicketStatus_shouldUpdateStatusSuccessfully() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Cannot login");
        ticket.setDescription("User cannot login to system");
        ticket.setCategory(TicketCategory.ACCOUNT_ISSUE);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setSuggestedReply("Please reset your password.");
        ticket.setClassificationSource(ClassificationSource.GEMINI);
        ticket.setStatus(TicketStatus.OPEN);

        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(1L);
        updatedTicket.setTitle("Cannot login");
        updatedTicket.setDescription("User cannot login to system");
        updatedTicket.setCategory(TicketCategory.ACCOUNT_ISSUE);
        updatedTicket.setPriority(TicketPriority.HIGH);
        updatedTicket.setSuggestedReply("Please reset your password.");
        updatedTicket.setClassificationSource(ClassificationSource.GEMINI);
        updatedTicket.setStatus(TicketStatus.RESOLVED);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.saveAndFlush(any(Ticket.class))).thenReturn(updatedTicket);

        TicketResponse result = ticketService.updateTicketStatus(1L, "resolved");

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(TicketStatus.RESOLVED.getLabel());

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).saveAndFlush(any(Ticket.class));
    }

    @Test
    void getAllTickets_shouldReturnTickets() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Cannot login");
        ticket.setDescription("User cannot login to system");
        ticket.setCategory(TicketCategory.ACCOUNT_ISSUE);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setSuggestedReply("Please reset your password.");
        ticket.setClassificationSource(ClassificationSource.GEMINI);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(ticket));

        List<TicketResponse> result = ticketService.getAllTickets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Cannot login");

        verify(ticketRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }
}