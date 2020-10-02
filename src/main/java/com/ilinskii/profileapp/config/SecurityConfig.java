package com.ilinskii.profileapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api-key}")
    private String apiKey;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/exit-success.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(apiKeyAuthFilter())
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .authorizeRequests()
                .antMatchers("/exit").permitAll()
                .anyRequest().authenticated();
    }

    private APIKeyAuthFilter apiKeyAuthFilter() {
        APIKeyAuthFilter filter = new APIKeyAuthFilter();

        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (!apiKey.equals(principal)) {
                throw new BadCredentialsException("Incorrect API Key");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        return filter;
    }

    private static class APIKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

        @Override
        protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
            return httpServletRequest.getHeader("X-API-Key");
        }

        @Override
        protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
            return null;
        }
    }
}
