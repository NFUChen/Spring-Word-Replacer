package com.zona.wordReplacer.web

import com.zona.wordReplacer.web.interceptors.AuthInterceptor
import com.zona.wordReplacer.web.interceptors.LoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        val intercepts:Array<HandlerInterceptor>  = arrayOf(
            LoggingInterceptor(),
            AuthInterceptor()
        )
        var idx = 0
        intercepts.forEach {
            interceptor  -> registry.addInterceptor(interceptor).order(idx)
            idx += 1
        }
    }
}