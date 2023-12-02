package com.zona.wordReplacer.repository

import com.zona.wordReplacer.entity.SensitiveWord
import org.springframework.data.repository.CrudRepository
import java.util.UUID


interface SensitiveWordRepository: CrudRepository<SensitiveWord, UUID>