package dev.ankit.springai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAgenticAIDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringAgenticAIDemo.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Batman Agentic AI API")
                        .version("1.0.0")
                        .description("Agentic AI demo using Spring AI with Batman knowledge base RAG")
                        .contact(new Contact()
                                .name("Batman RAG API")
                                .url("https://github.com")));
    }

}
