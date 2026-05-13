package com.example.AISupportTicketSystem.dto;

import com.example.AISupportTicketSystem.entity.ClassificationSource;
import com.example.AISupportTicketSystem.entity.TicketCategory;
import com.example.AISupportTicketSystem.entity.TicketPriority;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiTicketAnalysisResult {
    private TicketCategory category;
    private TicketPriority priority;
    private String suggestedReply;
    private ClassificationSource classificationSource;
}
