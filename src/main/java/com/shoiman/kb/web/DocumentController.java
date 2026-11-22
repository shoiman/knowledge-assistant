package com.shoiman.kb.web;

import com.shoiman.kb.service.IngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DocumentController {

    private final IngestionService ingestionService;

    public DocumentController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/documents")
    public Map<String, Integer> ingest(@RequestBody String text) {
        return Map.of("chunks", ingestionService.ingest(text));
    }
}
