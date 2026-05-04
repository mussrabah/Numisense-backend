package com.numisence.numisensebackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                // Allow public access to auth endpoints and websocket handshake
                auth.requestMatchers("/api/v1/auth/**", "/ws-numiterra/**").permitAll()
                // In a real production setup, uncomment below to secure the rest:
                // auth.anyRequest().authenticated()
                auth.anyRequest().permitAll() // Left open temporarily for easy local testing
            }

        // Note: A real implementation would also add a JwtAuthenticationFilter here
        // to intercept requests and populate the SecurityContext.

        return http.build()
    }
}