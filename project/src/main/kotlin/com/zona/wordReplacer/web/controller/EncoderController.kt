package com.zona.wordReplacer.web.controller

import com.zona.wordReplacer.service.*
import com.zona.wordReplacer.web.Response
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/encoder")
class EncoderController(
    val encoderService: EncoderService,
    val authService: AuthService,
    val memberService: MemberService,
    val rateLimitService: RateLimitService
) {

    @PostMapping("/encode")
    fun encode(@RequestBody encoderRequest: EncoderRequest, request: HttpServletRequest): Response<ArrayList<EncoderView>> {


        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }
        val memberId = authService.getMemberId(targetCookie?.value ?: "")
        val member = memberService.findMemberById(memberId)
        if (rateLimitService.isLimited(member)) {
            return Response(arrayListOf(), HttpStatus.FORBIDDEN)
        }

        rateLimitService.limitOnMember(member)



        return Response(encoderService.encode(encoderRequest.content, member))
    }

    @GetMapping("/count")
    fun getCount(request: HttpServletRequest): Response<Int> {
        val targetCookie = request.cookies?.find {
            it.name == authService.KEY
        }
        val memberId = authService.getMemberId(targetCookie?.value ?: "")
        val member = memberService.findMemberById(memberId)
        return Response(rateLimitService.getRemainingCount(member))

    }

}