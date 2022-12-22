package com.mauntung.mauntung.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.adapter.http.security.JwtUserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final RsaKeyProperties rsaKeys;
    private final JwtUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(JwtUserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public JwtUserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeRequests(auth -> auth
                .mvcMatchers("/merchant/auth/*", "/customer/auth/*").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(configurer -> configurer.authenticationEntryPoint(this::prepareUnauthorizedResponse))
            .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.getPublicKey()).privateKey(rsaKeys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    private Map<String, Object> buildUnauthorizedResponseBody(AuthenticationException ex) {
        Map<String, Object> responseMap = new HashMap<>();
        if (ex instanceof BadCredentialsException) {
            responseMap.put("errors", List.of("Bad Credentials"));
        } else {
            responseMap.put("errors", List.of("Unauthorized"));
        }
        return responseMap;
    }

    private void prepareUnauthorizedResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        Map<String, Object> responseBody = buildUnauthorizedResponseBody(ex);
        ObjectMapper mapper = new ObjectMapper();
        String responseMsg = mapper.writeValueAsString(responseBody);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseMsg);
    }
}
