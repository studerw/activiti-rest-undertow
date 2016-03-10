package com.studerw.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.rest.common.application.ContentTypeResolver;
import org.activiti.rest.common.application.DefaultContentTypeResolver;
import org.activiti.rest.service.api.RestResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
//@PropertySources({
//        @PropertySource(value = "classpath:db.properties", ignoreResourceNotFound = true),
//        @PropertySource(value = "classpath:engine.properties", ignoreResourceNotFound = true)
//})
//@ComponentScan(basePackages = {"com.studerw"})
@ImportResource({"classpath:spring/applicationContext.xml"})
public class ApplicationConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Bean()
    public RestResponseFactory restResponseFactory() {
        log.debug("Creating restResponseFactory bean...");
        RestResponseFactory restResponseFactory = new RestResponseFactory();
        return restResponseFactory;
    }

    @Bean()
    public ContentTypeResolver contentTypeResolver() {
        log.debug("Creating contentTypeResolver bean...");
        ContentTypeResolver resolver = new DefaultContentTypeResolver();
        return resolver;
    }

    @Bean
    public ObjectMapper objectMapper(){
        log.debug("Creating ObjectMapper bean...");
        return new ObjectMapper();
    }



}