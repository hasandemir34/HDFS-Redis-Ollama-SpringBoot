package com.sau.empsystem.controller;

import com.sau.empsystem.dto.FootballerDto;
import com.sau.empsystem.model.Footballer;
import com.sau.empsystem.service.FootballerService;
import com.sau.empsystem.service.HdfsService;
import com.sau.empsystem.service.OllamaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class FootballerController {

    private final FootballerService footballerService;
    private final HdfsService hdfsService;
    private final OllamaService ollamaService;

    @Value("${hdfs.web.url}")
    private String hdfsWebUrl;

    public FootballerController(FootballerService footballerService,
                                HdfsService hdfsService,
                                OllamaService ollamaService) {
        this.footballerService = footballerService;
        this.hdfsService = hdfsService;
        this.ollamaService = ollamaService;
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

    @GetMapping("/footballers")
    @ResponseBody
    public List<FootballerDto> getAllFootballers() {
        return footballerService.getAllFootballerDetails();
    }

    @GetMapping("/footballers/{id}")
    @ResponseBody
    public ResponseEntity<Footballer> getFootballer(@PathVariable Integer id) {
        return footballerService.findFootballer(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/footballers")
    @ResponseBody
    public Footballer createFootballer(@RequestBody Footballer footballer) {
        return footballerService.saveFootballer(footballer);
    }

    @PutMapping("/footballers/{id}")
    @ResponseBody
    public ResponseEntity<Footballer> updateFootballer(@PathVariable Integer id,
                                                        @RequestBody Footballer footballer) {
        Optional<Footballer> existing = footballerService.findFootballer(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        footballer.setFootballerid(id);
        return ResponseEntity.ok(footballerService.saveFootballer(footballer));
    }

    @DeleteMapping("/footballers/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFootballer(@PathVariable Integer id) {
        if (footballerService.findFootballer(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        footballerService.deleteFootballer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/footballers/{id}/photo")
    @ResponseBody
    public ResponseEntity<?> uploadPhoto(@PathVariable Integer id,
                                         @RequestParam("file") MultipartFile file) {
        Optional<Footballer> existing = footballerService.findFootballer(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Dosya seçilmedi.");
        }

        try {
            Footballer footballer = existing.get();
            footballer.setPhoto(hdfsService.upload(file));
            return ResponseEntity.ok(footballerService.saveFootballer(footballer));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Dosya HDFS'e yüklenemedi.");
        }
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public String chatApi(@RequestBody Map<String, String> payload) {
        return ollamaService.chatWithLlama(payload.get("prompt"));
    }
}
