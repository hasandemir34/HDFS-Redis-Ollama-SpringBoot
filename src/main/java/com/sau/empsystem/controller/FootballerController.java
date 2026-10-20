package com.sau.empsystem.controller;

import com.sau.empsystem.dto.FootballerDto;
import com.sau.empsystem.model.Footballer;
import com.sau.empsystem.service.FootballerService;
import com.sau.empsystem.service.OllamaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FootballerController {

    private final FootballerService footballerService;
    private final OllamaService ollamaService;

    public FootballerController(FootballerService footballerService, OllamaService ollamaService) {
        this.footballerService = footballerService;
        this.ollamaService = ollamaService;
    }

    @Value("${hdfs.web.url:http://127.0.0.1:9870/webhdfs/v1/emp_images/}")
    private String hdfsWebUrl;

    // =========================================================
    // 🌐 WEB ARAYÜZÜ (THYMELEAF) ENDPOINT'LERİ
    // =========================================================

    // Tarayıcı için ana sayfa (futbolcu tablosu).
    @GetMapping("/")
    public String index(Model model) {
        List<FootballerDto> footballers = footballerService.getAllFootballerDetails();
        model.addAttribute("footballers", footballers);
        model.addAttribute("hdfsWebUrl", hdfsWebUrl);
        return "index";
    }

    // Tarayıcı için Chat Sayfası
    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    // =========================================================
    // 🚀 REST API (POSTMAN) ENDPOINT'LERİ
    // =========================================================

    // 1. Tüm futbolcuları JSON olarak döner (Redis cache testi).
    @GetMapping("/footballers")
    @ResponseBody
    public List<FootballerDto> getAllFootballersApi() {
        return footballerService.getAllFootballerDetails();
    }

    // 2. Yeni futbolcu ekler ve Redis cache'ini temizler.
    @PostMapping("/footballers")
    @ResponseBody
    public Footballer createFootballerApi(@RequestBody Footballer footballer) {
        return footballerService.saveFootballer(footballer);
    }

    // 3. Postman POST Testi & Web Chat: Yapay Zeka Chat API
    @PostMapping("/api/chat")
    @ResponseBody
    public String chatApi(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        return ollamaService.chatWithLlama(prompt);
    }
}