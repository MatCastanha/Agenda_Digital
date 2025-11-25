package com.example.agenda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Agenda de Contatos") // Título da sua API
                        .version("v1.0.0") // Versão da sua API
                        .description("API RESTful para gerenciamento de contatos, incluindo operações CRUD e busca por nome/telefone."));
    }
}