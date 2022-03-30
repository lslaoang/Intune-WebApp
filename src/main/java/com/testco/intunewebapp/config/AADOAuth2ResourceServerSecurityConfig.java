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
        // super.configure(http);
        // @formatter:off
        http
                .authorizeRequests(auth -> auth
                        .antMatchers(HttpMethod.GET, "/check").permitAll()
//                        .antMatchers(HttpMethod.GET, "/verify").permitAll()
                        .antMatchers(HttpMethod.GET, "/verify").hasAuthority("SCOPE_Consumer.Read") /* Authenticate user accessing this endpoint */
                        .anyRequest().authenticated());
        // @formatter:on
    }
}