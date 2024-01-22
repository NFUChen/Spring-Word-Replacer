package com.zona.wordReplacer.web.controller

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.auth.UserRole
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RateLimitService {
    val rateLimitMap: MutableMap<Long, Int> = mutableMapOf()
    val logger = LoggerFactory.getLogger(this::class.java)
    companion object {
        const val MAX_TRIAL = 2
    }
    fun limitOnMember(member: Member) {
        val memberRoles = member.roles.map { UserRole.fromString(it.role)}
        if (UserRole.GUEST in memberRoles) {
            rateLimitMap[member.id!!] = 1 + (rateLimitMap[member.id] ?: 0)
            logger.warn("[GUEST RATE LIMIT] Current member: ${member} is a Guest, add rate limit count by 1, current count: ${rateLimitMap[member.id!!]}")
        }
    }

    fun isLimited(member: Member): Boolean {
        return (rateLimitMap[member.id] ?: 0) >= MAX_TRIAL
    }

    fun getRemainingCount(member: Member): Int {
        return 2 - (rateLimitMap[member.id] ?: 0)
    }
}