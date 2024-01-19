package com.zona.wordReplacer.web.controller

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.auth.MemberView
import com.zona.wordReplacer.service.AuthService
import com.zona.wordReplacer.service.MemberService
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
    val authService: AuthService,
    val memberService: MemberService
) {
    @PostMapping("/public/login")
    fun login(@RequestBody form: LoginForm, request: HttpServletRequest, response: HttpServletResponse): Response<MemberView> {
        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }

        val cookieSid = targetCookie?.value ?: ""
        if (authService.isValidSessionId(cookieSid)) {
            val memberId = authService.getMemberId(cookieSid)

            return Response(memberService.findMemberById(memberId).toView(), HttpStatus.ACCEPTED)
        }

        val sid = authService.login(
            form.email, form.password
        )
        val cookie = Cookie(authService.KEY, sid)
        cookie.maxAge = 24 * 3600
        response.addCookie(
            cookie
        )
        val memberId = authService.getMemberId(sid)

        return Response(memberService.findMemberById(memberId).toView(), HttpStatus.ACCEPTED)
    }

    @PostMapping("/public/signup")
    fun signUpNewMember(@RequestBody newMember: Member): Response<MemberView> {

        memberService.signUpNewMember(newMember)
        return Response(
            newMember.toView(),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): Response<String> {
        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }
        targetCookie?.maxAge = -1
        authService.logout(targetCookie?.value!!)
        return Response("Logout successfully", HttpStatus.ACCEPTED)
    }


    @GetMapping("/user")
    fun getUserId(request: HttpServletRequest): Response<MemberView> {
        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }
        val memberId = authService.getMemberId(targetCookie?.value!!)
        val member = memberService.findMemberById(memberId)
        return Response(member.toView())
    }





}