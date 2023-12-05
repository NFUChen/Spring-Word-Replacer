package com.zona.wordReplacer.controller

import com.zona.wordReplacer.service.AuthService
import com.zona.wordReplacer.web.Response
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


data class LoginForm(
    val email: String,
    val password: String
)
@RestController
@RequestMapping("/api")
class AuthController(
    val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody form: LoginForm, request: HttpServletRequest ,response: HttpServletResponse): Response<String> {
        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }
        if (authService.isValidSessionId(targetCookie?.value ?: "")) {
            return Response("Already login", HttpStatus.ACCEPTED)
        }
        val sid = authService.login(
            form.email, form.password
        )
        val cookie = Cookie(authService.KEY, sid)
        cookie.maxAge = 24 * 3600
        response.addCookie(
            cookie
        )
        return Response("Login successfully", HttpStatus.ACCEPTED)
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): Response<String> {
        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }
        targetCookie?.maxAge = -1
        authService.logout(targetCookie?.value ?: "")
        return Response("Logout successfully", HttpStatus.ACCEPTED)
    }



}