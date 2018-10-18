package org.cloud.webtemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {

    	List<Parameter> pars = new ArrayList<Parameter>();
    	
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("Authorization")
                .defaultValue("this-is-token")
                .description("token")
                .modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        
        ParameterBuilder formPar = new ParameterBuilder();
        formPar.name("from")
        		.defaultValue("app")
        		.description("请求来源")
        		.modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(formPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.cloud.cms.controller.api"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口中心")
                .description("")
                .version("1.0")
                .build();
    }

}