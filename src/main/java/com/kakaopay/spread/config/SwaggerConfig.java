package com.kakaopay.spread.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title("Spread API")
                        .description("Spread API List")
                        .contact(new Contact("spread",
                                "https://github.com/kakaopay1004/spread",
                                "choikyusun47@naver.com"))
                        .version("1.0.0").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kakaopay.spread.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);

    }

}