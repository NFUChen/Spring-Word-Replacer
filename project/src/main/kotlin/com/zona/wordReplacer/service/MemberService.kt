package com.zona.wordReplacer.service

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.auth.MemberView
import com.zona.wordReplacer.entity.auth.Role
import com.zona.wordReplacer.entity.auth.UserRole
import com.zona.wordReplacer.repository.MemberRepository
import com.zona.wordReplacer.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    val memberRepository: MemberRepository,
    val roleRepository: RoleRepository,
    val authService: AuthService,
) {

    fun findAllMembers(): Iterable<Member> {
        return memberRepository.findAll()
    }

    fun findMemberById(id: Long): Member {
        val optionalMember = memberRepository.findById(id)
        if (!optionalMember.isPresent) {
            throw IllegalArgumentException("Member ID: $id not found")
        }
        return optionalMember.get()
    }

    @Transactional
    fun addMemberAsGuest(member: Member): MemberView {
        if (isMemberExistByEmail(member.email)) {
            throw IllegalArgumentException("Member email: ${member.email} already exists")
        }

        member.roles = mutableListOf(
            Role(UserRole.GUEST.roleName, member)
        )
        memberRepository.save(member)
        return member.toView()
    }



    @Transactional
    fun updateMemberById(memberId: Long,updatedMember: Member): Member {
        val member = findMemberById(memberId)

        member.email = updatedMember.email ?: member.email
        member.name = updatedMember.name ?: member.name

        memberRepository.save(member)
        return member
    }

    @Transactional
    fun updateMemberRolesById(memberId: Long, roleStrings: Iterable<String>): Member {
        roleRepository.deleteByMemberId(memberId)

        val userRoles = roleStrings.map {
            UserRole.fromString(it)
        }
        val member = findMemberById(memberId)
        val roles = userRoles.map {
            Role(it.roleName, member)
        }
        roleRepository.saveAll(roles)
        member.roles = roles.toMutableList()
        return member
    }

    fun isMemberExistByEmail(email: String?): Boolean {
        if (email == null) {
            throw IllegalArgumentException("Can not find email with NULL value")
        }
        return memberRepository.existsByEmail(email)
    }

    fun getAllRoles(): Iterable<String> {
        return UserRole.values().toSet().map {
            it.toString()
        }
    }
    fun setMemberPassword(memberId: Long, newPassword: String) {
        val memberFound = findMemberById(memberId)
        memberFound.password = authService.encodePassword(newPassword)
        memberRepository.save(memberFound)
    }

}