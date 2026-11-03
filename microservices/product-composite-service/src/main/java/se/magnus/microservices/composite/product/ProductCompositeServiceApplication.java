package se.magnus.microservices.composite.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication
@ComponentScan ("se.magnus")
public class ProductCompositeServiceApplication {

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
		restTemplate.getMessageConverters().add(jsonConverter);

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
