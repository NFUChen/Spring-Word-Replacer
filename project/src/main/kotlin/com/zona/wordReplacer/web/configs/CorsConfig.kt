package com.zona.wordReplacer.web.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        // Allow requests from these origins
        config.addAllowedOrigin("/**")
        // You can add more origins as needed

        // Allow specific headers
        config.addAllowedHeader("Origin")
        config.addAllowedHeader("Content-Type")
        config.addAllowedHeader("Accept")
        config.addAllowedHeader("Authorization")
        // You can add more headers as needed

        // Allow specific HTTP methods (GET, POST, PUT, etc.)
        config.addAllowedMethod("GET")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("PUT")
        config.addAllowedMethod("DELETE")
        // You can add more methods as needed

        // Allow credentials (cookies)
        config.allowCredentials = true

        // Set the max age of the CORS options response
        config.maxAge = 3600L
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}