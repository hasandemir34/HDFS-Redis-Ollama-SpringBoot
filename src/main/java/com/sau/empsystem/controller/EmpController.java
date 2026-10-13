package com.sau.empsystem.controller;

import com.sau.empsystem.dto.EmpDto;
import com.sau.empsystem.service.EmpService;
import com.sau.empsystem.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class EmpController {

    @Autowired
    private EmpService empService;

    @Autowired
    private OllamaService ollamaService;

    @Value("${hdfs.web.url:http://127.0.0.1:9870/webhdfs/v1/emp_images/}")
    private String hdfsWebUrl;

    // 1. Sayfa: Personel Tablosu (Sayfa İstekleri)
    @GetMapping("/")
    public String index(Model model) {
        List<EmpDto> employees = empService.getAllEmpDetails();
        model.addAttribute("employees", employees);
        model.addAttribute("hdfsWebUrl", hdfsWebUrl);
        return "index";
    }

    // 2. Sayfa: Chat Sayfası Yönlendirmesi
    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    // Chat İstekleri İçin AJAX Endpoint'i
    @PostMapping("/api/chat")
    @ResponseBody
    public String chatApi(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        return ollamaService.chatWithLlama(prompt);
    }
}