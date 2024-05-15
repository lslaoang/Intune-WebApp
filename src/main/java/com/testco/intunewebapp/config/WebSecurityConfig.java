package com.testco.intunewebapp.config;

import com.testco.intunewebapp.handler.IntuneAccessDeniedHandler;
import com.testco.intunewebapp.handler.IntuneAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class WebSecurityConfig {

    private final IntuneAccessDeniedHandler intuneAccessDeniedHandler;
    private final IntuneAuthenticationEntryPoint intuneAuthenticationEntryPoint;

    public WebSecurityConfig(IntuneAccessDeniedHandler intuneAccessDeniedHandler,
                             IntuneAuthenticationEntryPoint intuneAuthenticationEntryPoint) {
        this.intuneAccessDeniedHandler = intuneAccessDeniedHandler;
        this.intuneAuthenticationEntryPoint = intuneAuthenticationEntryPoint;
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .exceptionHandling(exception -> exception.accessDeniedHandler(intuneAccessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(intuneAuthenticationEntryPoint))
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig
                                .policyDirectives("default-src 'none'")))
                .authorizeHttpRequests(requests -> requests

                        .requestMatchers(HttpMethod.GET, "/health").anonymous()
                        .requestMatchers(HttpMethod.GET, "/api/v1/intune/version").anonymous()
                        .requestMatchers(HttpMethod.GET, "/api/v1/intune/version-check").anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/v1/intune/upload-file").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/intune/feedback").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/intune/verify").hasAuthority("SCOPE_Consumer.read")
                        .requestMatchers(HttpMethod.GET, "/api/v1/verify").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/verify").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/file-upload").authenticated()
                        .anyRequest()
                        .denyAll());
        // @formatter:on
        return http.build();
    }
}