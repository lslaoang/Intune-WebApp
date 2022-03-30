package com.testco.intunewebapp.config;

import com.azure.spring.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AADOAuth2ResourceServerSecurityConfig extends AADResourceServerWebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // @formatter:off
        http
                .authorizeRequests(auth -> auth
                        .mvcMatchers("/health").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/check").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/verify").hasAuthority("SCOPE_Consumer.read")
                        .anyRequest().authenticated());
        // @formatter:on
    }
}