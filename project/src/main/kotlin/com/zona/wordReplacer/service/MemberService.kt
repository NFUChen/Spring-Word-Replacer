package com.zona.wordReplacer.service

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.auth.MemberView
import com.zona.wordReplacer.entity.auth.Role
import com.zona.wordReplacer.entity.auth.UserRole
import com.zona.wordReplacer.repository.MemberRepository
import com.zona.wordReplacer.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional
@Service
class MemberService(
    val memberRepository: MemberRepository,
    val roleRepository: RoleRepository,
    val authService: AuthService,
) {

    fun findAllMembers(): Iterable<Member> {
        return memberRepository.findAll().filter { !it.isRole(UserRole.ADMIN) }
    }

    fun findMemberById(id: Long): Member {
        val optionalMember = memberRepository.findById(id)
        if (!optionalMember.isPresent) {
            throw IllegalArgumentException("Member ID: $id not found")
        }
        return optionalMember.get()
    }

    fun signUpNewMember(member: Member): MemberView {

        val encodedPassword = authService.encodePassword(
            member.password ?: throw IllegalArgumentException("Password for encoding can not be null")
        )
        member.password = encodedPassword

        if (isMemberExistByEmail(member.email)) {
            throw IllegalArgumentException("Member email: ${member.email} already exists")
        }

        member.roles = mutableListOf(
            Role(UserRole.GUEST.roleName, member)
        )
        memberRepository.save(member)
        return member.toView()
    }


    fun updateMemberById(memberId: Long,updatedMember: Member): Member {
        val member = findMemberById(memberId)

        member.email = updatedMember.email ?: member.email
        member.name = updatedMember.name ?: member.name

        memberRepository.save(member)
        return member
    }

    fun updateMemberRolesById(memberId: Long, userRoles: Iterable<UserRole>): Member {

        roleRepository.deleteByMemberId(memberId)

        val member = findMemberById(memberId)
        val roles = userRoles.map {
            Role(it.roleName, member)
        }
        roleRepository.saveAll(roles)
        member.roles = roles.toMutableList()
        return member
    }

    fun updateMemberRolesStringById(memberId: Long, roleStrings: Iterable<String>): Member {
        val userRoles = roleStrings.map {
            UserRole.fromString(it)
        }
        return updateMemberRolesById(memberId, userRoles)
    }

    fun isMemberExistByEmail(email: String?): Boolean {
        if (email == null) {
            throw IllegalArgumentException("Can not find email with NULL value")
        }
        return memberRepository.existsByEmail(email)
    }

    fun getAllRoles(): Iterable<String> {
        return UserRole.values().filter { it != UserRole.ADMIN }.toSet().map {
            it.toString()
        }
    }
    fun setMemberPassword(memberId: Long, newPassword: String) {
        val memberFound = findMemberById(memberId)
        memberFound.password = authService.encodePassword(newPassword)
        memberRepository.save(memberFound)
    }

    fun activateMemberById(memberId: Long): MemberView {
        val member = findMemberById(memberId)

        member.isActivated = true
        memberRepository.save(member)

        return member.toView()
    }

    fun deactivateMemberById(memberId: Long): MemberView {
        val member = findMemberById(memberId)

        member.isActivated = false
        memberRepository.save(member)

        return member.toView()



    }

}