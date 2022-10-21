package com.testco.intunewebapp.config;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "apigee")
public class ApigeeConfig {

    String uploadEndpoint;
    String verifyEndpoint;
    String clientId;

}
