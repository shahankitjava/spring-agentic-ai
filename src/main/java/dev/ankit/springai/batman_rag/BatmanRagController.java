package dev.ankit.springai.batman_rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/batman")
@Tag(name = "Batman RAG", description = "Batman knowledge base RAG endpoints")
public class BatmanRagController {

    @Autowired
    private BatmanRagService batmanRagService;

    @PostMapping("/initialize")
    @Operation(summary = "Initialize Batman Knowledge Base", description = "Load and vectorize Batman.pdf into PostgreSQL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Knowledge base initialized successfully"),
        @ApiResponse(responseCode = "500", description = "Error loading PDF or connecting to database")
    })
    public String initializeBatmanKb() {
        batmanRagService.loadBatmanPdfToVectorStore();
        return "Batman knowledge base initialized successfully!";
    }

    @PostMapping("/query")
    @Operation(summary = "Query Batman Knowledge", description = "Ask questions about Batman character using RAG")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query processed successfully", 
            content = @Content(schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Error processing query")
    })
    public String queryBatman(@RequestBody BatmanQueryRequest request) {
        return batmanRagService.queryBatmanKnowledge(request.getQuestion());
    }

    @GetMapping("/search")
    @Operation(summary = "Search Similar Content", description = "Find similar documents in Batman knowledge base")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Query parameter is required"),
        @ApiResponse(responseCode = "500", description = "Error searching knowledge base")
    })
    public List<Document> searchBatmanContent(@RequestParam String query) {
        return batmanRagService.findSimilarBatmanContent(query);
    }

    @Schema(description = "Request body for Batman knowledge queries")
    public static class BatmanQueryRequest {

        @Schema(description = "Question about Batman", example = "Who is Batman and what are his powers?")
        private String question;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }

}
