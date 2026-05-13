package com.example.AISupportTicketSystem.service;

import com.example.AISupportTicketSystem.dto.AiTicketAnalysisResult;

public interface GeminiClassificationService {
    AiTicketAnalysisResult analyzeTicket(String title, String description);
}
