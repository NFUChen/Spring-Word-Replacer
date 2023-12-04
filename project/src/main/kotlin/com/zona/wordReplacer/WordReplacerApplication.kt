package com.zona.wordReplacer

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.service.MemberService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


@SpringBootApplication
class WordReplacerApplication

fun main(args: Array<String>) {
	runApplication<WordReplacerApplication>(*args)
}

@Component
class CommandLineRunner(
	val memberService: MemberService
) : CommandLineRunner {

	override fun run(vararg args: String?) {
		// Your code to be executed on application startup
		val memberView = memberService.addMemberAsGuest(
			Member("admin", "admin@admin.com", "")
		)

		memberService.setMemberPassword(memberView.memberId ?: 1, "zonazona-admin")
		memberService.updateMemberRolesById(memberView.memberId ?: 1, arrayListOf(
			"ROLE_ADMIN"
		))
	}
}