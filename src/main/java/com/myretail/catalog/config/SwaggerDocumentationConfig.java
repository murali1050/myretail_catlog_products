package com.myretail.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerDocumentationConfig {

  /** customImplementation. */
  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.myretail.catalog.api"))
        .build()
        .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
        .apiInfo(apiInfo());
  }

  /** apiInfo. */
  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("My Retail Catalog Service")
        .description("Get Product details and pricing details for the given product IDs")
        .license("")
        .licenseUrl("")
        .termsOfServiceUrl("")
        .version("0.1.0")
        .contact(new Contact("", "", ""))
        .build();
  }
}
