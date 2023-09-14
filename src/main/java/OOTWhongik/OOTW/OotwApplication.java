package OOTWhongik.OOTW;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@ConfigurationPropertiesScan
@EnableJpaAuditing
@SpringBootApplication
public class OotwApplication {

	public static void main(String[] args) {
		SpringApplication.run(OotwApplication.class, args);
	}

}
