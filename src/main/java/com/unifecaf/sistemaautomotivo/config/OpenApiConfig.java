package com.unifecaf.sistemaautomotivo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sistemaAutomotivoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Sistema Automotivo - API de Gestão de Estoque de Veículos")
                .version("1.0.0")
                .description("""
                        API REST para gestão de estoque de veículos de uma concessionária.
                        Permite cadastrar marcas, modelos e veículos (carros, motos e
                        caminhões), além de consultar com filtros, atualizar e remover.""")
                .contact(new Contact().name("UniFECAF - Programação Orientada a Objetos"))
                .license(new License().name("Uso acadêmico")));
    }
}
