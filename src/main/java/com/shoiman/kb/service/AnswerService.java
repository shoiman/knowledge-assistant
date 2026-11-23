package com.shoiman.kb.service;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private static final String NO_ANSWER = "В документах нет ответа на этот вопрос.";

    private final SearchService searchService;
    private final ChatModel chatModel;

    public AnswerService(SearchService searchService, ChatModel chatModel) {
        this.searchService = searchService;
        this.chatModel = chatModel;
    }

    public String answer(String question) {
        List<EmbeddingMatch<TextSegment>> matches = searchService.search(question);

        // 1. если ничего не найдено, вернуть NO_ANSWER, не вызывая модель
        if (matches.isEmpty()) {
            return NO_ANSWER;
        }

        // 2. склеить тексты найденных частей в один контекст (через пустую строку между ними)
        String context = matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n\n"));

        // 3. собрать промпт и вызвать модель
        String prompt = """
                Ответь на вопрос, используя только приведённый контекст.
                Если в контексте нет ответа, напиши: "В документах нет ответа на этот вопрос."

                Контекст:
                %s

                Вопрос: %s
                """.formatted(context, question);

        return chatModel.chat(prompt);
    }
}
