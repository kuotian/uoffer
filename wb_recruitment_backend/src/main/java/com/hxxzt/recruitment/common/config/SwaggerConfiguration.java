package com.hxxzt.recruitment.common.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
//@Profile({"dev"})
public class SwaggerConfiguration {

    @Bean
    public Docket platformApi() {

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).forCodeGeneration(true)
                .groupName("前台管理界面接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(regex("^.*(?<!error)$"))
                .paths(PathSelectors.regex("/adminApi.*"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping("/")
                ;


    }

    @Bean
    public Docket platformApi2() {

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).forCodeGeneration(true)
                .groupName("小程序相关接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.regex("/wxApi.*"))
                .build()
                .securitySchemes(customerSecuritySchemes())
                .securityContexts(customerSecurityContexts())
                .pathMapping("/")
                ;


    }

    private List<ApiKey> securitySchemes() {
        return Lists.newArrayList(
                new ApiKey("Authorization", "Authorization", "header"));
    }

    private List<ApiKey> customerSecuritySchemes() {
        return Lists.newArrayList(
                new ApiKey("WXAuthorization", "WXAuthorization", "header"));
    }


    private List<SecurityContext> securityContexts() {
        return Lists.newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .build()
        );
    }

    private List<SecurityContext> customerSecurityContexts() {
        return Lists.newArrayList(
                SecurityContext.builder()
                        .securityReferences(customerDefaultAuth())
                        .build()
        );
    }


    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "认证权限");
        return Lists.newArrayList(
                new SecurityReference("Authorization", new AuthorizationScope[]{authorizationScope}));
    }

    List<SecurityReference> customerDefaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "认证权限");
        return Lists.newArrayList(
                new SecurityReference("WXAuthorization", new AuthorizationScope[]{authorizationScope}));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("招聘小程序接口").description("©2020 Copyright. Powered By 南风知我意.")
                .termsOfServiceUrl("")
                .contact(new Contact("南风知我意", "https://hxxzt.com", "niko_hxx@163.com"))
                .license("版权所有_南风知我意博客")
                .licenseUrl("https://hxxzt.com")
                .version("2.0")
                .build();
    }

}