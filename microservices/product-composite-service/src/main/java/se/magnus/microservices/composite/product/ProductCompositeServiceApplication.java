package se.magnus.microservices.composite.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication
@ComponentScan("se.magnus")
public class ProductCompositeServiceApplication {
	@Value("${api.common.version}")			String apiVersion;
	@Value("${api.common.title}")			String apiTitle;
	@Value("${api.common.description}")		String apiDescription;
	@Value("${api.common.termsOfService}")	String apiTermsOfService;
	@Value("${api.common.license}")			String apiLicense;
	@Value("${api.common.licenseUrl}")		String apiLicenseUrl;
	@Value("${api.common.externalDocDesc}")	String apiExternalDocDesc;
	@Value("${api.common.externalDocUrl}")	String apiExternalDocUrl;
	@Value("${api.common.contact.name}")	String apiContactName;
	@Value("${api.common.contact.url}")		String apiContactUrl;
	@Value("${api.common.contact.email}")	String apiContactEmail;

	@Bean 
	public OpenAPI getOpenApiDocumentation() {
		return new OpenAPI()
			.info(new Info().title(apiTitle)
				.description(apiDescription)
				.version(apiVersion)
				.contact(new Contact()
					.name(apiContactName)
					.url(apiContactUrl)
					.email(apiContactEmail))
				.termsOfService(apiTermsOfService)
				.license(new License()
					.name(apiLicense)
					.url(apiLicenseUrl)))
			.externalDocs(new ExternalDocumentation()
				.description(apiExternalDocDesc)
				.url(apiExternalDocUrl));
	}

	private final Integer threadPoolSize;
	private final Integer taskQueueSize;

	public ProductCompositeServiceApplication(
		@Value("${app.threadPoolSize:10}") Integer threadPoolSize,
		@Value("${app.taskQueueSize:100}") Integer taskQueueSize
	) {
		this.threadPoolSize = threadPoolSize;
		this.taskQueueSize = taskQueueSize;
	}

	@Bean
	Scheduler publishEventScheduler() {
		return Schedulers.newBoundedElastic(10, 100, "publish-event");
	}

	// @Bean 
	// RestTemplate restTemplate() {
	// 	return new RestTemplate();
	// }

	@Bean
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		var jsonConverter = new JacksonJsonHttpMessageConverter(
			JsonMapper.builder()
				.enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
		);

		// 기존 JSON 변환기를 설정이 적용된 변환기로 교체
		restTemplate.getMessageConverters().removeIf(
			converter -> converter instanceof JacksonJsonHttpMessageConverter
		);
		restTemplate.getMessageConverters().add(0, jsonConverter);

		return restTemplate;
	}

	@Bean
	JsonMapperBuilderCustomizer allowFinalFieldsAsMutators() {
		return builder ->
			builder.enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}
}
