package com.testco.intunewebapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
//        super.configure(httpSecurity);

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                        .mvcMatchers(HttpMethod.GET,"/health").permitAll()
                        .mvcMatchers(HttpMethod.GET,"/api/v1/verify").permitAll()
                        .anyRequest().authenticated()
                .and()
                .headers()
                .contentSecurityPolicy("default-src 'none'");

    }
}
