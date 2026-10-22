package com.sau.empsystem.controller;

import com.sau.empsystem.dto.FootballerDto;
import com.sau.empsystem.model.Footballer;
import com.sau.empsystem.service.FootballerService;
import com.sau.empsystem.service.OllamaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FootballerController {

    private final FootballerService footballerService;
    private final OllamaService ollamaService;
    private final String hdfsWebUrl;

    public FootballerController( //hdfs bağlantısı
            FootballerService footballerService,
            OllamaService ollamaService,
            @Value("${hdfs.web.url}") String hdfsWebUrl) {
        this.footballerService = footballerService;
        this.ollamaService = ollamaService;
        this.hdfsWebUrl = hdfsWebUrl;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("footballers", footballerService.getAllFootballerDetails());
        model.addAttribute("hdfsWebUrl", hdfsWebUrl);
        return "index";
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    @GetMapping("/footballers/all")
    @ResponseBody
    public List<FootballerDto> getAllFootballers() {
        return footballerService.getAllFootballerDetails();
    }

    @GetMapping("/footballers/{id}")
    @ResponseBody
    public ResponseEntity<Footballer> getFootballerById(@PathVariable Integer id) {
        return ResponseEntity.of(java.util.Optional.ofNullable(footballerService.findFootballer(id)));
    }

    @PostMapping("/footballers/add")
    @ResponseBody
    public Footballer createFootballer(@RequestBody Footballer footballer) {
        return footballerService.saveFootballer(footballer);
    }

    @PutMapping("/footballers/{id}")
    @ResponseBody
    public ResponseEntity<Footballer> updateFootballer(@PathVariable Integer id, @RequestBody Footballer footballer) {
        if (footballerService.findFootballer(id) != null) {
            footballer.setFootballerid(id);
            return ResponseEntity.ok(footballerService.saveFootballer(footballer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/footballers/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteFootballer(@PathVariable Integer id) {
        if (footballerService.findFootballer(id) == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Futbolcu bulunamadı."));
        }

        footballerService.deleteFootballer(id);
        return ResponseEntity.ok(Map.of("message", "Futbolcu başarıyla silindi."));
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public String chatApi(@RequestBody Map<String, String> payload) {
        return ollamaService.chatWithLlama(payload.get("prompt"));
    }
}