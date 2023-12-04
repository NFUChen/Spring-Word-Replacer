package com.zona.wordReplacer.web.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityConfig {
    @Bean
    fun bcrpytPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}