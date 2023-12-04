package com.zona.wordReplacer.controller

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.auth.MemberView
import com.zona.wordReplacer.service.MemberService
import com.zona.wordReplacer.web.Response
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/members")
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
    fun addNewMemberAsGuest(@RequestBody newMember: Member): Response<MemberView> {

        memberService.addMemberAsGuest(newMember)
        return Response(
            newMember.toView(),
            HttpStatus.CREATED
        )
    }

    @PutMapping("/{id}")
    fun updateMember(@PathVariable id: Long,  @RequestBody member: Member): Response<MemberView> {
        return Response(
            memberService.updateMemberById(id,member).toView(),
            HttpStatus.ACCEPTED
        )
    }
    @PutMapping("/password/{id}")
    fun updatePassword(@PathVariable id: Long, @RequestBody password: String): Response<String> {
        memberService.setMemberPassword(id, password)
        return Response("Update password successfully")
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