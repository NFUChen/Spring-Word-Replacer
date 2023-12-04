package com.zona.wordReplacer.web.interceptors

import com.zona.wordReplacer.service.AuthService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception


class AuthInterceptor(
    val authService: AuthService
): HandlerInterceptor {
    val logger = LoggerFactory.getLogger(this::class.simpleName)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val cookies = request.cookies
        val targetCookie = cookies?.find {
            it.name.equals(authService.KEY)
        } ?: throw IllegalArgumentException("Access denied: ${authService.KEY} not found, please login first")

        if (!authService.isValidSessionId(targetCookie.value)) {
            throw IllegalArgumentException("Access denied: invalid SID, please login again")
        }

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {}

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)
    }
}