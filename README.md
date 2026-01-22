# Spring Agentic AI Demo

A Spring Boot application demonstrating **Retrieval-Augmented Generation (RAG)** using Spring AI with a Batman knowledge base. This project showcases how to build intelligent, context-aware AI applications using Ollama, PostgreSQL with pgvector, and Spring AI.

## Overview

This application implements an agentic AI system that:
- **Loads and vectorizes** PDF documents into a vector database
- **Retrieves relevant documents** using semantic similarity search
- **Generates context-aware responses** using an LLM (Ollama with Llama3)
- **Provides REST APIs** for initialization, querying, and searching

## Features

✅ **Document Loading** - Load and parse PDF documents (Batman.pdf)  
✅ **Vector Storage** - Store document embeddings in PostgreSQL with pgvector  
✅ **Semantic Search** - Find similar documents using vector similarity  
✅ **LLM Integration** - Query local Ollama LLM for intelligent responses  
✅ **REST APIs** - Interactive endpoints with full OpenAPI/Swagger documentation  
✅ **Metadata Enrichment** - Enhance documents with source and classification metadata  

## Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.5.9 |
| **AI/ML** | Spring AI | 1.0.0-M6 |
| **Language Model** | Ollama (Llama3) | Latest |
| **Vector Database** | PostgreSQL with pgvector | 13+ |
| **API Documentation** | SpringDoc OpenAPI (Swagger) | 2.6.0 |
| **Build Tool** | Maven | 4.0.0 |
| **Java Version** | 24 |

## Prerequisites

Before running this application, ensure you have installed:

- **Java 24** ([SDKMAN](https://sdkman.io/) recommended)
- **Maven 3.6+**
- **Docker & Docker Compose** (for PostgreSQL)
- **Ollama** with Llama3 model pulled (`ollama pull llama3`)

## Project Structure

```
spring-agentic-ai-demo/
├── src/main/java/dev/ankit/springai/
│   ├── SpringAgenticAIDemo.java           # Main Spring Boot application
│   ├── batman_rag/
│   │   ├── BatmanRagController.java       # REST API endpoints
│   │   ├── BatmanRagService.java          # Business logic for RAG
│   │   └── BatmanQueryRequest.java        # DTO for query requests
│   └── config/
│       ├── RagConfiguration.java          # Spring AI beans configuration
│       └── RagInitializer.java            # Database initialization
├── src/main/resources/
│   ├── application.properties             # Application configuration
│   └── Batman.pdf                         # Knowledge base document
├── pom.xml                                # Maven dependencies
└── README.md
```

## Setup & Installation

### 1. Clone the Repository
```bash
git clone https://github.com/shahankitjava/spring-agentic-ai.git
```

### 2. Start PostgreSQL with pgvector

Using Docker Compose (create `docker-compose.yml`):
```yaml
version: '3.8'
services:
  postgres:
    image: pgvector/pgvector:0.7.0-pg16
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgresql
      POSTGRES_DB: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

Run: `docker-compose up -d`

### 3. Start Ollama Server

```bash
ollama serve
```

In another terminal, pull the Llama3 model:
```bash
ollama pull llama3
```

### 4. Build the Application

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Swagger UI
Access interactive API documentation at: **http://localhost:8080/swagger-ui.html**

### Endpoints

#### 1. Initialize Batman Knowledge Base
```
POST /api/batman/initialize
```
Loads and vectorizes the Batman.pdf document into PostgreSQL.

**Response:**
```json
"Batman knowledge base initialized successfully!"
```

#### 2. Query Batman Knowledge
```
POST /api/batman/query
Content-Type: application/json

{
  "question": "What are Batman's superpowers?"
}
```
Retrieves relevant documents and generates an AI-powered response.

**Response:**
```json
"Batman is not a superhero with superpowers. Instead, he relies on his intellect, martial arts training, advanced technology, and detective skills..."
```

#### 3. Search Similar Content
```
GET /api/batman/search?query=Batman gadgets
```
Finds documents semantically similar to the query.

**Response:**
```json
[
  {
    "content": "Batman's utility belt contains...",
    "metadata": {
      "source": "Batman.pdf",
      "type": "character_profile",
      "page_number": "5"
    }
  }
]
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Ollama Server
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<password>

# Vector Store
spring.ai.vectorstore.pgvector.initialize-schema=true
spring.ai.vectorstore.pgvector.dimensions=1024
```

## How RAG Works

1. **Document Ingestion** → PDF is loaded and split into pages
2. **Embedding** → Each document chunk is converted to vector embeddings via Ollama
3. **Storage** → Vectors are stored in PostgreSQL with pgvector extension
4. **Query Processing** → User question is converted to an embedding
5. **Similarity Search** → Find most similar documents in vector store
6. **Context Generation** → Build prompt with retrieved documents
7. **LLM Response** → Ollama generates answer based on context

## Example Usage

### Using cURL

Initialize the knowledge base:
```bash
curl -X POST http://localhost:8080/api/batman/initialize
```

Query the knowledge base:
```bash
curl -X POST http://localhost:8080/api/batman/query \
  -H "Content-Type: application/json" \
  -d '{"question": "Who is Batman?"}'
```

Search for similar content:
```bash
curl "http://localhost:8080/api/batman/search?query=Gotham+City"
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 5432 already in use | Change PostgreSQL port in `docker-compose.yml` |
| Ollama connection refused | Ensure `ollama serve` is running on port 11434 |
| Model not found error | Run `ollama pull llama3` |
| pgvector extension not found | Docker image already includes pgvector |
| OutOfMemoryError | Increase JVM heap: `export _JAVA_OPTIONS=-Xmx2g` |

## Development

### Build
```bash
mvn clean package
```

### Test
```bash
mvn test
```

### Debug
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

## Architecture Diagram

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP
       ▼
┌─────────────────────────┐
│   Spring Boot REST API  │
│   BatmanRagController   │
└──────┬──────┬───────────┘
       │      │
   ┌───▼──┐   │
   │  RAG │   │ Query
   │Logic │   │
   └──┬───┘   │
      │       │
      ▼       ▼
┌─────────────────────┐
│ BatmanRagService    │
│ • Load PDF          │
│ • Vectorize         │
│ • Search Similar    │
│ • Generate Response │
└──┬───────────┬──────┘
   │           │
   ▼           ▼
┌──────────┐  ┌─────────┐
│PostgreSQL│  │ Ollama  │
│+pgvector │  │ Llama3  │
└──────────┘  └─────────┘
```

## Performance Considerations

- **Vector Dimensions**: Set to 1024 (configurable for different models)
- **Similarity Search**: Returns top-k most relevant documents
- **Ollama Model**: Llama3 (7B/13B variants available)
- **Database Indexing**: pgvector automatically creates IVFFLAT indexes for large datasets

## Future Enhancements

- [ ] Support multiple PDF documents
- [ ] Implement conversational memory (chat history)
- [ ] Add document source attribution in responses
- [ ] Support different embedding models
- [ ] Add authentication/authorization
- [ ] Implement response caching
- [ ] Add batch processing for large document sets
- [ ] Support for real-time document updates

## Contributing

Contributions are welcome! Please ensure:
- Code follows Spring Boot conventions
- Tests are included for new features
- Documentation is updated accordingly

## License

This project is provided as-is for educational and demonstration purposes.

## Support

For issues, questions, or suggestions:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review Spring AI documentation: https://docs.spring.io/spring-ai/
3. Ollama documentation: https://ollama.ai/

## References

- [Spring AI Documentation](https://docs.spring.io/spring-ai/)
- [Ollama](https://ollama.ai/)
- [PostgreSQL pgvector](https://github.com/pgvector/pgvector)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [RAG Pattern](https://cloud.google.com/use-cases/retrieval-augmented-generation)

---

**Created by:** Ankit Shah
**Last Updated:** January 2026  
**Status:** Active Development
