package dev.ankit.springai.batman_rag;

import java.util.List;
import java.util.Objects;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class BatmanRagService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OllamaEmbeddingModel embeddingModel;

    /**
     * Load and vectorize Batman PDF document
     */
    public void loadBatmanPdfToVectorStore() {
        try {
            Resource resource = resourceLoader.getResource("classpath:Batman.pdf");

            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
            List<Document> documents = pdfReader.get();

            // Add metadata to documents
            documents.forEach(doc -> {
                doc.getMetadata().put("source", "Batman.pdf");
                doc.getMetadata().put("type", "character_profile");
                doc.getMetadata().put("page_number", doc.getMetadata().getOrDefault("page_number", "unknown"));
            });

            // Store vectors in PostgreSQL
            vectorStore.add(documents);

            System.out.println("Successfully loaded and vectorized " + documents.size() + " documents from Batman.pdf");
        } catch (Exception e) {
            System.err.println("Error loading Batman PDF: " + e.getMessage());
        }
    }

    /**
     * Query Batman knowledge using RAG
     */
    public String queryBatmanKnowledge(String question) {
        // Retrieve relevant documents from vector store
        List<Document> similarDocuments = vectorStore.similaritySearch(question);

        // Build context from retrieved documents
        StringBuilder context = new StringBuilder();
        context.append("Based on the Batman knowledge base:\n\n");
        if (similarDocuments != null) {

            context.append(similarDocuments.get(0).getFormattedContent()).append("\n\n");
        }

        // Create prompt with context
        String prompt = context.toString() + "\n\nQuestion: " + question + "\n\nAnswer:";

        // Generate response using Ollama LLM
        return chatClient.prompt(prompt).call().content();
    }

    /**
     * Get similar documents for a query
     */
    public List<Document> findSimilarBatmanContent(String query) {
        return vectorStore.similaritySearch(query);
    }

}
