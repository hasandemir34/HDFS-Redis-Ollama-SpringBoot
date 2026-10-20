package com.sau.empsystem.controller;

import com.sau.empsystem.dto.EmpDto;
import com.sau.empsystem.model.Emp;
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

    // =========================================================
    // 🌐 WEB ARAYÜZÜ (THYMELEAF) ENDPOINT'LERİ
    // =========================================================

    // Tarayıcı için Ana Sayfa (Çalışan Tablosu)
    @GetMapping("/")
    public String index(Model model) {
        List<EmpDto> employees = empService.getAllEmpDetails();
        model.addAttribute("employees", employees);
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

    // 1. Postman GET Testi: Tüm çalışanları JSON olarak döner (Redis Cache Testi)
    @GetMapping("/api/employees")
    @ResponseBody
    public List<EmpDto> getAllEmployeesApi() {
        return empService.getAllEmpDetails();
    }

    // 2. Postman POST Testi: Yeni çalışan ekler (Redis @CacheEvict Temizleme Testi)
    @PostMapping("/api/employees")
    @ResponseBody
    public Emp createEmployeeApi(@RequestBody Emp emp) {
        return empService.saveEmp(emp);
    }

    // 3. Postman POST Testi & Web Chat: Yapay Zeka Chat API
    @PostMapping("/api/chat")
    @ResponseBody
    public String chatApi(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        return ollamaService.chatWithLlama(prompt);
    }
}