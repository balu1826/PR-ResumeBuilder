package com.talentstream.service;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.talentstream.AwsSecretsManagerUtil;
import com.talentstream.dto.AIInterviewPrepBotDTO;
import com.talentstream.dto.AIPrepChatDTO;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.ApplicantSkills;
import com.talentstream.repository.AIPrepChatRepository;
import com.talentstream.repository.ApplicantProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClientException;

import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class ResumeAIEnhancerService {
	private static final Logger logger = LoggerFactory.getLogger(AIInterviewPrepBotService.class);
	@Autowired

	private RestTemplate restTemplate;
	@Autowired

	private AIPrepChatService aiPrepChatService;
	@Autowired
	private ApplicantProfileRepository applicantProfileRepository;

	@Autowired
	private AIPrepChatRepository aiPrepChatRepository;

	private String groqApiKey;

	private String groqApiUrl = "https://api.groq.com/openai/v1/chat/completions";

	private String groqModel = "llama-3.1-8b-instant";
	
	private synchronized void initializeGroqApiKey() {

	    if (groqApiKey != null)
	        return;

	    logger.debug("initializeGroqApiKey - fetching Groq API key from AWS Secrets Manager");

	    try {
	        String secret = AwsSecretsManagerUtil.getSecret();
	        JsonObject json = JsonParser.parseString(secret).getAsJsonObject();

	        groqApiKey = json.get("GROQ_API_KEY").getAsString();

	    } catch (Exception e) {
	        logger.error("initializeGroqApiKey - failed to load Groq API key", e);
	        throw new RuntimeException("Failed to load Groq API key from AWS Secrets Manager");
	    }
	}


	
	public String enhanceSummary(String summary, String role, String jd) {

	    logger.info("Enhancing resume summary using Groq AI");

	    try {
	        initializeGroqApiKey();

	        String prompt;

	        if (jd != null && jd.trim().length() > 1) {
	            prompt = "Enhance the following resume summary to align with the provided Job Description.\n" +
	                    "Use relevant skills, keywords, and responsibilities from the JD to make it ATS-friendly and professional.\n" +
	                    "Output must be 3-4 lines, concise, and factual.\n" +
	                    "Do NOT add markdown, bullets, JSON, or headings.\n\n" +
	                    "SUMMARY:\n" + summary + "\n\n" +
	                    "JOB DESCRIPTION:\n" + jd + "\n\n" +
	                    "Return only the enhanced summary text.";
	        } else {
	            prompt = "Enhance the following resume summary to suit the role of '" + role + "'.\n" +
	                    "Use relevant role keywords to improve ATS score.\n" +
	                    "Output must be 3-4 lines, concise, professional, and factual.\n" +
	                    "Do NOT add markdown, bullets, JSON, or headings.\n\n" +
	                    "SUMMARY:\n" + summary + "\n\n" +
	                    "Return only the enhanced summary text.";
	        }

	        // Prepare messages
	        JsonArray messages = new JsonArray();

	        JsonObject systemMsg = new JsonObject();
	        systemMsg.addProperty("role", "system");
	        systemMsg.addProperty("content",
	                "You are an expert ATS resume enhancement assistant.");
	        messages.add(systemMsg);

	        JsonObject userMsg = new JsonObject();
	        userMsg.addProperty("role", "user");
	        userMsg.addProperty("content", prompt);
	        messages.add(userMsg);

	        // Request body
	        JsonObject requestBody = new JsonObject();
	        requestBody.addProperty("model", groqModel);
	        requestBody.add("messages", messages);
	        requestBody.addProperty("temperature", 0.3);
	        requestBody.addProperty("max_tokens", 400);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + groqApiKey);

	        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

	        logger.debug("Sending enhanceSummary request to Groq API");

	        ResponseEntity<String> response =
	                restTemplate.exchange(groqApiUrl, HttpMethod.POST, entity, String.class);
	        String responseBody = response.getBody();
	        JsonObject jsonResponse =
	                JsonParser.parseString(response.getBody()).getAsJsonObject();

	        JsonArray choices = jsonResponse.getAsJsonArray("choices");

	        if (choices != null && choices.size() > 0) {
	            return choices.get(0)
	                    .getAsJsonObject()
	                    .getAsJsonObject("message")
	                    .get("content")
	                    .getAsString()
	                    .trim();
	        }

	        return responseBody; // fallback

	    } catch (Exception e) {
	        logger.error("Error enhancing summary: {}", e.getMessage(), e);
	        return summary; // fallback to original
	    }
	}


	

	
}
