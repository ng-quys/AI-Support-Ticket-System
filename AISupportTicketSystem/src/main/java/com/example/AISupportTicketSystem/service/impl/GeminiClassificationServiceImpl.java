package com.example.AISupportTicketSystem.service.impl;

import com.example.AISupportTicketSystem.dto.AiTicketAnalysisResult;
import com.example.AISupportTicketSystem.entity.ClassificationSource;
import com.example.AISupportTicketSystem.entity.TicketCategory;
import com.example.AISupportTicketSystem.entity.TicketPriority;
import com.example.AISupportTicketSystem.service.GeminiClassificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiClassificationServiceImpl implements GeminiClassificationService {

    private static final String PAYMENT_REPLY = "Thank you for reporting this payment issue. We understand your money may have been deducted. Our support team will verify the transaction and update you as soon as possible.";
    private static final String LOGIN_REPLY = "Thank you for reporting this login issue. Please make sure your credentials are correct. Our support team will help you regain access as soon as possible.";
    private static final String TECHNICAL_REPLY = "Thank you for reporting this technical problem. Our team will investigate the crash or error and get back to you as soon as possible.";
    private static final String ACCOUNT_REPLY = "Thank you for contacting us about your account information. Our support team will review your request and assist you shortly.";
    private static final String OTHER_REPLY = "Thank you for contacting support. Our team will review your issue and get back to you soon.";
    private static final Pattern JSON_BLOCK_PATTERN = Pattern.compile("```(?:json)?\\s*(\\{.*?})\\s*```", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("(\\{.*})", Pattern.DOTALL);

    private final ObjectMapper objectMapper;
    private final RestClient.Builder restClientBuilder;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiApiUrl;

    @Override
    public AiTicketAnalysisResult analyzeTicket(String title, String description) {
        boolean keyPresent = geminiApiKey != null && !geminiApiKey.isBlank();
        log.info("Gemini key present: {}", keyPresent);
        if (!keyPresent) {
            log.warn("Gemini API key missing. Using keyword fallback classification.");
            return fallback(title, description);
        }

        try {
            RestClient restClient = restClientBuilder.build();

            String prompt = buildPrompt(title, description);
            ResponseEntity<String> response = restClient.post()
                    .uri(geminiApiUrl + "?key=" + geminiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(buildRequestBody(prompt))
                    .retrieve()
                    .toEntity(String.class);

            log.info("Gemini HTTP status: {}", response.getStatusCode().value());
            String responseBody = response.getBody();
            if (responseBody != null && !responseBody.isBlank()) {
                log.info("Gemini raw response (sanitized): {}", sanitizeForLog(responseBody));
            }
            return parseResponse(responseBody, title, description);
        } catch (Exception exception) {
            log.warn("Gemini analysis failed. Using keyword fallback classification. reason={}", exception.getMessage());
            return fallback(title, description);
        }
    }

    private AiTicketAnalysisResult parseResponse(String responseBody, String title, String description) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (textNode.isMissingNode() || textNode.asText().isBlank()) {
                log.warn("Gemini response missing candidate text. Using keyword fallback.");
                return fallback(title, description);
            }

            String extractedJson = extractJson(textNode.asText());
            if (extractedJson == null || extractedJson.isBlank()) {
                log.warn("Gemini response did not contain a parsable JSON object. Using keyword fallback.");
                return fallback(title, description);
            }

            JsonNode aiJson = objectMapper.readTree(extractedJson);
            TicketCategory category = TicketCategory.fromLabel(aiJson.path("category").asText());
            TicketPriority priority = TicketPriority.fromLabel(aiJson.path("priority").asText());
            String suggestedReply = aiJson.path("suggestedReply").asText();

            if (suggestedReply == null || suggestedReply.isBlank()) {
                suggestedReply = replyForCategory(category);
            }

            return AiTicketAnalysisResult.builder()
                    .category(category)
                    .priority(priority)
                    .suggestedReply(suggestedReply.trim())
                    .classificationSource(ClassificationSource.GEMINI)
                    .build();
        } catch (Exception exception) {
            log.warn("Gemini returned invalid JSON. Using keyword fallback. reason={}", exception.getMessage());
            return fallback(title, description);
        }
    }

    private Object buildRequestBody(String prompt) {
        return java.util.Map.of(
                "contents", java.util.List.of(
                        java.util.Map.of(
                                "parts", java.util.List.of(
                                        java.util.Map.of("text", prompt)
                                )
                        )
                )
        );
    }

    private String buildPrompt(String title, String description) {
        return "You are an AI support classifier for a fintech support desk. Return ONLY valid JSON, no markdown, no code fence, no explanation. "
                + "Use this exact schema: "
                + "{\n"
                + "  \"category\": \"Payment Issue\",\n"
                + "  \"priority\": \"High\",\n"
                + "  \"suggestedReply\": \"...\"\n"
                + "}\n"
                + "Allowed category values exactly: Authentication Issue, Payment Issue, Account Issue, Technical Bug, General Inquiry, Other.\n"
                + "Allowed priority values exactly: Low, Medium, High.\n"
                + "Classification rules:\n"
                + "- If title or description mentions payment, transaction, money deducted, transfer, failed payment, charge, charged, charged twice, refund, debit, card charged => category MUST be Payment Issue.\n"
                + "- If mentions cannot login, can not login, password, OTP, account access, sign in, login failed, authentication => category MUST be Authentication Issue.\n"
                + "- If mentions phone number, profile, account info, KYC, verification, personal details => category MUST be Account Issue.\n"
                + "- If mentions app crash, bug, error, freeze, stuck, technical problem => category MUST be Technical Bug.\n"
                + "- If issue is a normal question/request and does not fit the above, category MUST be General Inquiry or Other.\n"
                + "- If payment failed and money was deducted, priority should usually be High or Medium, never Low unless clearly minor.\n"
                + "- suggestedReply should be professional, short, and helpful.\n\n"
                + "Title: " + title + "\n"
                + "Description: " + description;
    }

    private String extractJson(String text) {
        Matcher fencedMatcher = JSON_BLOCK_PATTERN.matcher(text);
        if (fencedMatcher.find()) {
            return fencedMatcher.group(1).trim();
        }
        Matcher objectMatcher = JSON_OBJECT_PATTERN.matcher(text);
        if (objectMatcher.find()) {
            return objectMatcher.group(1).trim();
        }
        return text != null ? text.trim() : null;
    }

    private String sanitizeForLog(String responseBody) {
        String compact = responseBody.replaceAll("\\s+", " ").trim();
        if (compact.length() > 500) {
            return compact.substring(0, 500) + "...";
        }
        return compact;
    }

    private AiTicketAnalysisResult fallback(String title, String description) {
        String content = ((title == null ? "" : title) + " " + (description == null ? "" : description)).toLowerCase();

        if (containsAny(content, "payment", "transaction", "money deducted", "deducted", "transfer", "refund", "charge")) {
            return buildResult(TicketCategory.PAYMENT_ISSUE, TicketPriority.HIGH, ClassificationSource.KEYWORD_FALLBACK);
        }
        if (containsAny(content, "login", "password", "sign in", "signin", "authentication")) {
            return buildResult(TicketCategory.AUTHENTICATION_ISSUE, TicketPriority.MEDIUM, ClassificationSource.KEYWORD_FALLBACK);
        }
        if (containsAny(content, "crash", "bug", "error", "freeze", "app keeps crashing")) {
            return buildResult(TicketCategory.TECHNICAL_BUG, TicketPriority.HIGH, ClassificationSource.KEYWORD_FALLBACK);
        }
        if (containsAny(content, "account", "phone number", "profile", "kyc")) {
            return buildResult(TicketCategory.ACCOUNT_ISSUE, TicketPriority.LOW, ClassificationSource.KEYWORD_FALLBACK);
        }
        return buildResult(TicketCategory.OTHER, TicketPriority.MEDIUM, ClassificationSource.DEFAULT_FALLBACK);
    }

    private boolean containsAny(String content, String... keywords) {
        for (String keyword : keywords) {
            if (content.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private AiTicketAnalysisResult buildResult(TicketCategory category, TicketPriority priority,
                                               ClassificationSource classificationSource) {
        return AiTicketAnalysisResult.builder()
                .category(category)
                .priority(priority)
                .suggestedReply(replyForCategory(category))
                .classificationSource(classificationSource)
                .build();
    }

    private String replyForCategory(TicketCategory category) {
        return switch (category) {
            case PAYMENT_ISSUE -> PAYMENT_REPLY;
            case AUTHENTICATION_ISSUE -> LOGIN_REPLY;
            case TECHNICAL_BUG -> TECHNICAL_REPLY;
            case ACCOUNT_ISSUE -> ACCOUNT_REPLY;
            case GENERAL_INQUIRY, OTHER -> OTHER_REPLY;
        };
    }
}
