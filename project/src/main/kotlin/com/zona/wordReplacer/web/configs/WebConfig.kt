package com.zona.wordReplacer.web.configs

import com.zona.wordReplacer.service.AuthService
import com.zona.wordReplacer.service.MemberService
import com.zona.wordReplacer.web.interceptors.AdminRouteInterceptor
import com.zona.wordReplacer.web.interceptors.AuthInterceptor
import com.zona.wordReplacer.web.interceptors.LoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    val authService: AuthService,
    val memberService: MemberService
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LoggingInterceptor())
        registry.addInterceptor(AuthInterceptor(authService)).excludePathPatterns("/api/public/**")
        registry.addInterceptor(AdminRouteInterceptor(authService, memberService)).addPathPatterns("/api/admin/**")


    }
}