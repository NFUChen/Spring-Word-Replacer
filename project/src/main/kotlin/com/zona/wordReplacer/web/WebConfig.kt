package com.zona.wordReplacer.web

import com.zona.wordReplacer.service.AuthService
import com.zona.wordReplacer.web.interceptors.AuthInterceptor
import com.zona.wordReplacer.web.interceptors.LoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    val authService: AuthService
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(
            AuthInterceptor(authService)).excludePathPatterns("/api/login")
        registry.addInterceptor(LoggingInterceptor())

    }
}