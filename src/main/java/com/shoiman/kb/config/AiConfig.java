package com.shoiman.kb.config;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.chat.ChatModel;

@Configuration
public class AiConfig {

    @Bean
    EmbeddingModel embeddingModel(@Value("${ollama.base-url}") String baseUrl) {
        return OllamaEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .modelName("nomic-embed-text")
                .build();
    }

     @Bean
     EmbeddingStore<TextSegment> embeddingStore(@Value("${pgvector.host}") String host,
        @Value("${pgvector.port}") int port,
        @Value("${pgvector.database}") String database,
        @Value("${pgvector.user}") String user,
        @Value("${pgvector.password}") String password) {
       return PgVectorEmbeddingStore.builder()
                .host(host)
                .port(port)
                .database(database)
                .table("embeddings")
                .dimension(768)
                .createTable(true)
                .user(user)
                .password(password)
                .build();
        
     }

     @Bean
     ChatModel
    chatModel(@Value("${ollama.base-url}") String baseUrl) {
            return dev.langchain4j.model.ollama.OllamaChatModel.builder()
                    .baseUrl(baseUrl)
                    .modelName("llama3.2:3b")
                    .build();
        }
}
