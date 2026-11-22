package com.shoiman.kb.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public IngestionService(EmbeddingModel embeddingModel,
                            EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public int ingest(String text) {
        Document document = Document.from(text);

        // 1. разрезать на части: ~500 символов, перекрытие 50
        List<TextSegment> segments = DocumentSplitters.recursive(500, 50).split(document);

        // 2. посчитать векторы (допишите вызов EmbeddingModel для списка частей)
        // .content() нужен, потому что результат обёрнут в Response;
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // 3. сохранить (допишите вызов EmbeddingStore для векторов и частей)
        //embeddingStore.addAll(embeddings, segments) сохраняет векторы вместе с их текстами.
        embeddingStore.addAll(embeddings, segments);

        return segments.size();
    }
}
