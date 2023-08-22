package OOTWhongik.OOTW;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing
@SpringBootApplication
public class OotwApplication {

	public static void main(String[] args) {
		SpringApplication.run(OotwApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000",
										"http://ootw.s3-website.ap-northeast-2.amazonaws.com/")
						.allowedMethods(
								HttpMethod.GET.name(),
								HttpMethod.POST.name(),
								HttpMethod.PUT.name(),
								HttpMethod.DELETE.name(),
								HttpMethod.PATCH.name(),
								HttpMethod.HEAD.name()
						);
			}
		};
	}
}
