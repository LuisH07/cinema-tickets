package com.es.cinema.tickets.config;

import com.es.cinema.tickets.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/error"
    };

    private static final String[] PUBLIC_CATALOG_ENDPOINTS = {
            "/filmes/**",
            "/salas/**",
            "/sessoes/**"
    };

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger/**",
            "/api-doc/**",
            "/api-doc/docs"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;


    private HttpSecurity baseSecurityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        return baseSecurityFilterChain(http)
                .authorizeHttpRequests(auth -> auth
                        // públicos
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // swagger permitido em dev
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()

                        // actuator totalmente aberto em dev
                        .requestMatchers("/actuator/**").permitAll()

                        // catálogo: somente GET público
                        .requestMatchers(HttpMethod.GET, PUBLIC_CATALOG_ENDPOINTS).permitAll()

                        // o resto exige login (e roles ficam no @PreAuthorize)
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        return baseSecurityFilterChain(http)
                .authorizeHttpRequests(auth -> auth
                        // públicos
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // swagger bloqueado em prod
                        .requestMatchers(SWAGGER_ENDPOINTS).denyAll()

                        // actuator: health público e resto ADMIN
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")

                        // catálogo: somente GET público
                        .requestMatchers(HttpMethod.GET, PUBLIC_CATALOG_ENDPOINTS).permitAll()

                        // qualquer outra coisa exige token
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}