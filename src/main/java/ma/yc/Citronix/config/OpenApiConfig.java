package ma.yc.Citronix.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${contact.email}") String email,
                                 @Value("${local-server.url}") String localServerUrl,
                                 @Value("${prod-server.url}") String prodServerUrl,
                                 @Value("${project.version}") String appVersion,
                                 @Value("${project.title}") String title,
                                 @Value("${project.description}") String description) {
        log.info("email: {}", email);
        log.info("localServerUrl: {}", localServerUrl);

        return new OpenAPI().info(new Info()
                .title(title)
                .version(appVersion)
                .description(description)
                .contact(new Contact()
                        .name("Codex Pigeon Sky Race API")
                        .email(email))
        ).servers(Arrays.asList(
                new Server().description("Local Docker Server").url(localServerUrl),
                new Server().description("Production Server (inshallah soon when i deploy on aws)").url(prodServerUrl)
        ));
    }
}
