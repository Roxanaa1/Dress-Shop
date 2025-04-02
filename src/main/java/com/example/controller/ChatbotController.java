package com.example.controller;

import com.example.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/chatbot")
public class ChatbotController {
    @Autowired
    private ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> message) {
        String prompt = message.get("message");
        String reply = chatbotService.askGpt(prompt);
        return ResponseEntity.ok(reply);
    }

}
