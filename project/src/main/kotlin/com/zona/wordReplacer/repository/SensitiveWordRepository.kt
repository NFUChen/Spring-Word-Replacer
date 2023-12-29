package com.zona.wordReplacer.repository

import com.zona.wordReplacer.entity.encoder.SensitiveWord
import org.springframework.data.repository.CrudRepository
import java.util.UUID


interface SensitiveWordRepository: CrudRepository<SensitiveWord, UUID> {
    fun existsByContent(content: String): Boolean
}