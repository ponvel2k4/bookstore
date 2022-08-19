package com.app.bookstore.config;

import static com.app.bookstore.constants.ApiConstants.BOOK;
import static com.app.bookstore.constants.ApiConstants.VERSION_1;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Book Store Application").apiInfo(apiInfo()).select()
				.paths(regex(VERSION_1 + BOOK + ".*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Book Store").description("Swagger documentation for Book Store")
				.version("1.0").build();
	}

}
