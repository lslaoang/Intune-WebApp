package com.testco.intunewebapp;

import com.testco.intunewebapp.config.ApigeeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableConfigurationProperties(ApigeeConfig.class)
public class IntuneWebAppApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IntuneWebAppApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(IntuneWebAppApplication.class, args);
    }

}
