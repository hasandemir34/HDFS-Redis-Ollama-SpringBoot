package com.sau.empsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    @Value("${ollama.api.url:http://localhost:11434/api/generate}")
    private String ollamaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chatWithLlama(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama3.2");
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);

            Map response = restTemplate.postForObject(ollamaUrl, requestBody, Map.class);
            if (response != null && response.containsKey("response")) {
                return (String) response.get("response");
            }
        } catch (Exception e) {
            return "Ollama Servis Hatası: " + e.getMessage();
        }
        return "Yanıt alınamadı.";
    }
}