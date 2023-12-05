package com.zona.wordReplacer.web.interceptors

import com.zona.wordReplacer.entity.auth.UserRole
import com.zona.wordReplacer.service.AuthService
import com.zona.wordReplacer.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception

class AdminRouteInterceptor(
    val authService: AuthService,
    val memberService: MemberService
): HandlerInterceptor {
    val logger = LoggerFactory.getLogger(this::class.simpleName)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val cookies = request.cookies
        val targetCookie = cookies?.find {
            it.name.equals(authService.KEY)
        } ?: throw IllegalArgumentException("Access denied: ${authService.KEY} not found, please login admin account first")

        val memberId = authService.getMemberId(targetCookie.value)
        val member = memberService.findMemberById(memberId)

        if (!member.isRole(UserRole.ADMIN)) {
            throw IllegalArgumentException("Access denied: please login admin account for accessing admin APIs")
        }

        logger.info("USER ID: ${member.id} is a admin account, API: ${request.method} ${request.requestURL} passing.")

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)
    }
}