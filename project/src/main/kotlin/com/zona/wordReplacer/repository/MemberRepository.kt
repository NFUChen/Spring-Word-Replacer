package com.zona.wordReplacer.repository

import com.zona.wordReplacer.entity.auth.Member
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MemberRepository: CrudRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<Member>
}