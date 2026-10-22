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

    private final RestTemplate restTemplate = new RestTemplate();  //HTTP istemcisi

    public String chatWithLlama(String prompt) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", "llama3.2:latest");
            request.put("prompt", prompt);
            request.put("stream", false);
//  Ollama'ya POST isteği atar ve dönen JSON yanıtını alır
            Map response = restTemplate.postForObject(ollamaUrl, request, Map.class);
            if (response != null && response.get("response") != null) {
                return response.get("response").toString();
            }
        } catch (Exception e) {
            return "Hata: " + e.getMessage();
        }
        return "Yanıt alınamadı.";
    }
}