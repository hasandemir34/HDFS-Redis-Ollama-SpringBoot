package com.sau.empsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
            requestBody.put("model", "llama3.2:latest");
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    ollamaUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody),
                    new ParameterizedTypeReference<>() {
                    });
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.get("response") instanceof String answer) {
                return answer;
            }
        } catch (Exception e) {
            return "Ollama Servis Hatası: " + e.getMessage();
        }
        return "Yanıt alınamadı.";
    }
}