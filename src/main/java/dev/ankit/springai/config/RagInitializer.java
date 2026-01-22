package dev.ankit.springai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.ankit.springai.batman_rag.BatmanRagService;

@Component
public class RagInitializer {

    @Autowired
    private BatmanRagService batmanRagService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRag() {
        System.out.println("Initializing Batman RAG system...");
        batmanRagService.loadBatmanPdfToVectorStore();
        System.out.println("Batman RAG system initialized successfully!");
    }
}
