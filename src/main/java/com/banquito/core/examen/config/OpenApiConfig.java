package com.banquito.core.examen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestión de Efectivo - BanQuito")
                        .version("1.0.0")
                        .description("API para el manejo de efectivo en ventanillas bancarias. " +
                                "Permite a los cajeros abrir turnos, procesar transacciones y cerrar turnos " +
                                "con control de denominaciones de billetes.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo BanQuito")
                                .email("desarrollo@banquito.com")
                                .url("https://www.banquito.com"))
                        .license(new License()
                                .name("Licencia Propietaria")
                                .url("https://www.banquito.com/licencia")));
    }
} 