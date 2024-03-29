package com.testco.intunewebapp.config;

import com.azure.spring.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter;
import com.testco.intunewebapp.handler.IntuneAccessDeniedHandler;
import com.testco.intunewebapp.handler.IntuneAuthenticationEntryPoint;
import com.testco.intunewebapp.handler.IntuneAuthenticationFailureFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AADOAuth2ResourceServerSecurityConfig extends AADResourceServerWebSecurityConfigurerAdapter {

    private final IntuneAccessDeniedHandler intuneAccessDeniedHandler;
    private final IntuneAuthenticationEntryPoint intuneAuthenticationEntryPoint;
    private final IntuneAuthenticationFailureFilter intuneAuthenticationFailureFilter;

    public AADOAuth2ResourceServerSecurityConfig(IntuneAccessDeniedHandler intuneAccessDeniedHandler,
                                                 IntuneAuthenticationEntryPoint intuneAuthenticationEntryPoint,
                                                 IntuneAuthenticationFailureFilter intuneAuthenticationFailureFilter) {
        this.intuneAccessDeniedHandler = intuneAccessDeniedHandler;
        this.intuneAuthenticationEntryPoint = intuneAuthenticationEntryPoint;
        this.intuneAuthenticationFailureFilter = intuneAuthenticationFailureFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // @formatter:off
        http
                .exceptionHandling()
                .accessDeniedHandler(intuneAccessDeniedHandler)
                .authenticationEntryPoint(intuneAuthenticationEntryPoint).and()
                .addFilter(intuneAuthenticationFailureFilter.bearerTokenAuthenticationFilter(authenticationManagerBean()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(requests -> requests
                        .mvcMatchers(HttpMethod.GET, "/health").anonymous()
//                        .mvcMatchers(HttpMethod.GET, "/api/v1/check").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/intune/version").anonymous()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/intune/version-check").anonymous()
                        .mvcMatchers(HttpMethod.POST, "/api/v1/intune/upload-file").authenticated()
                        .mvcMatchers(HttpMethod.POST, "/api/v1/intune/feedback").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/intune/verify").hasAuthority("SCOPE_Consumer.read")
//                        .mvcMatchers(HttpMethod.POST, "/api/v1/verify").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/verify").authenticated()
                        .mvcMatchers(HttpMethod.POST, "/api/v1/verify").authenticated()
                        .mvcMatchers(HttpMethod.POST, "/api/v1/file-upload").authenticated()
                        .anyRequest()
                        .denyAll())
                .headers()
                .contentSecurityPolicy("default-src 'none'");
        // @formatter:on
    }
}