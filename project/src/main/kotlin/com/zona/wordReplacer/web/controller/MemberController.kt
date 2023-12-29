package com.zona.wordReplacer.web.controller

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.auth.MemberView
import com.zona.wordReplacer.service.AuthService
import com.zona.wordReplacer.service.MemberService
import com.zona.wordReplacer.web.Response
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/admin/members")
class MemberController(
    val memberService: MemberService
) {
    @GetMapping("/")
    fun findAllMembers(): Response<Iterable<MemberView>> {
        return Response(memberService.findAllMembers().map {
            it.toView()
        })
    }

    @PostMapping("/")
    fun signUpNewMember(@RequestBody newMember: Member): Response<MemberView> {

        memberService.signUpNewMember(newMember)
        return Response(
            newMember.toView(),
            HttpStatus.CREATED
        )
    }

    @PutMapping("/roles/{id}")
    fun updateMemberRoles(@PathVariable id: Long,  @RequestBody roleString: String): Response<MemberView> {
        return Response(
            memberService.updateMemberRolesById(
                id,
                listOf(roleString)
            ).toView(),
            HttpStatus.ACCEPTED
        )
    }

    @GetMapping("/roles")
    fun getAllRoles(): Response<Iterable<String>> {
        return Response(memberService.getAllRoles())
    }
}

@RestController
@RequestMapping("/api/personal/members")
class PersonalMemberController(
    val memberService: MemberService,
    val authService: AuthService
) {
    @PutMapping("/{memberId}")
    fun updateMember(@PathVariable memberId: Long,  @RequestBody member: Member, @CookieValue(value= "JSID", required = true) cookieValue: String): Response<MemberView> {
        throwIfNotSelf(cookieValue, memberId)
        return Response(
            memberService.updateMemberById(memberId,member).toView(),
            HttpStatus.ACCEPTED
        )
    }
    @PutMapping("/password/{memberId}")
    fun updatePassword(@PathVariable memberId: Long, @RequestBody password: String, @CookieValue(value= "JSID", required = true) cookieValue: String): Response<String> {
        throwIfNotSelf(cookieValue, memberId)
        memberService.setMemberPassword(memberId, password)
        return Response("Update password successfully")
    }

    private fun throwIfNotSelf(sessionId: String, checkedMemberId: Long) {
        if (!authService.isSelf(sessionId, checkedMemberId)) {
            val currentMember = authService.getMemberId(sessionId)
            throw IllegalArgumentException("Only self can update personal info. Permission denied. JSID belongs to ${currentMember}, trying to update ${checkedMemberId}")
        }
    }


}