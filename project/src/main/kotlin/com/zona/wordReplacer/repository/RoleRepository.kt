package com.zona.wordReplacer.repository

import com.zona.wordReplacer.entity.auth.Role
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RoleRepository: CrudRepository<Role, UUID> {
    fun findByMemberId(memberId: Long)
    fun deleteByMemberId(memberId: Long)
}