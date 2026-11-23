package com.shoiman.kb.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    private final double minScore;

    public SearchService(EmbeddingModel embeddingModel,
                            EmbeddingStore<TextSegment> embeddingStore,
                            @Value("${rag.min-score}") double minScore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.minScore = minScore;
    }
    public List<EmbeddingMatch<TextSegment>> search(String question) {
        // 1. превратить вопрос в вектор (один текст, не список)
        Embedding queryEmbedding = embeddingModel.embed(question).content();

        // 2. собрать запрос поиска: вектор вопроса, до 3 результатов
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(minScore)
                .build();

        // 3. выполнить поиск в хранилище и вернуть найденные совпадения
        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);
        return result.matches();
    }
}
