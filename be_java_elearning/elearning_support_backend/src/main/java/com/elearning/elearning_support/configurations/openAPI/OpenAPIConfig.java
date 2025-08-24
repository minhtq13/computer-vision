package com.elearning.elearning_support.configurations.openAPI;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@ComponentScan(basePackages = "com.elearning.elearning_support.configurations")
public class OpenAPIConfig {

    @Value("${server.servlet.context-path:/e-learning/sp}")
    private String serverContextPath;

    private final String SWAGGER_UI_DEFAULT_PATH =  serverContextPath + "/swagger-ui/index.html";

    @Value("${app.host}")
    private String host;

    @Bean
    public OpenAPI openAPI() {
        final List<Server> lstServer =  List.of(new Server().url(host).description("Backend Server"));
        return new OpenAPI()
            .servers(lstServer)
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(
                new Components().addSecuritySchemes(
                    "Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer")
                )
            )
            .info(new Info()
                .contact(new Contact()
                .email("chiendao1808@gmail.com")
                .name("Chien Dao - Hanoi University of Science and Technology"))
                .version("1.0.0")
            );
    }
}
