package com.shoiman.kb.web;

import com.shoiman.kb.service.AnswerService;
import com.shoiman.kb.service.IngestionService;
import com.shoiman.kb.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DocumentController {

    private final IngestionService ingestionService;
    private final SearchService searchService;

    private final AnswerService answerService;

    public DocumentController(IngestionService ingestionService,
                              SearchService searchService,
                              AnswerService answerService) {
        this.ingestionService = ingestionService;
        this.searchService = searchService;
        this.answerService = answerService;
    }

    @PostMapping("/documents")
    public Map<String, Integer> ingest(@RequestBody String text) {
        return Map.of("chunks", ingestionService.ingest(text));
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam("q") String question) {
        return searchService.search(question).stream()
                .map(match -> Map.<String, Object>of(
                        "score", match.score(),
                        "text", match.embedded().text()))
                .toList();
    }

    @GetMapping("/ask")
    public Map<String, String> ask(@RequestParam("q") String question) {
        return Map.of("answer", answerService.answer(question));
    }
}
