package com.music.catalog.infra.config;

import com.music.catalog.infra.ratelimit.RateLimitFilter;
import com.music.catalog.infra.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Swagger e Documentação (Públicos)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Login (Público) - Importante: HttpMethod deve ser org.springframework.http.HttpMethod
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // 3. Endpoints de Álbuns e Artistas (Públicos para leitura, se desejar, ou protegidos)
                        // No passo anterior deixamos públicos para teste, mas agora com Login,
                        // o ideal é proteger ou deixar público apenas o GET.
                        // Vamos deixar o GET público e o resto protegido para seguir o teste de Rate Limit.
                        .requestMatchers(HttpMethod.GET, "/v1/artists/**", "/v1/albums/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()

                        // 4. Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated()
                )
                // Ordem dos Filtros: SecurityFilter (JWT) -> RateLimitFilter -> UsernamePasswordAuth
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter, SecurityFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}