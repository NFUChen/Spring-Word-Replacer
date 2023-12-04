package com.zona.wordReplacer.repository

import com.zona.wordReplacer.entity.encoder.LegalWord
import org.springframework.data.repository.CrudRepository
import java.util.UUID


interface LegalWordRepository: CrudRepository<LegalWord, UUID>