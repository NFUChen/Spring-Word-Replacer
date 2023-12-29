package com.zona.wordReplacer.repository

import com.zona.wordReplacer.entity.encoder.LegalWord
import com.zona.wordReplacer.entity.encoder.SensitiveWord
import org.springframework.data.repository.CrudRepository
import java.util.UUID


interface LegalWordRepository: CrudRepository<LegalWord, UUID> {
    fun findLegalWordsBySensitiveWord(sensitiveWord: SensitiveWord)
}