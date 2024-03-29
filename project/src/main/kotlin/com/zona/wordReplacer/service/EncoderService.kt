package com.zona.wordReplacer.service

import com.zona.wordReplacer.entity.auth.Member
import com.zona.wordReplacer.entity.encoder.LegalWord
import com.zona.wordReplacer.entity.encoder.SensitiveWord
import com.zona.wordReplacer.repository.LegalWordRepository
import com.zona.wordReplacer.repository.SensitiveWordRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Service
class EncoderService(
    val sensitiveWordRepository: SensitiveWordRepository,
    val legalWordRepository: LegalWordRepository
) {
    val logger = LoggerFactory.getLogger(this::class.java)
    fun findSensitiveWordById(sensitiveWordId: UUID): SensitiveWord {
        val optinalSensitiveWord = sensitiveWordRepository.findById(sensitiveWordId)
        if (!optinalSensitiveWord.isPresent) {
            throw IllegalArgumentException("Sensitive Word ID: $sensitiveWordId not found")
        }

        return optinalSensitiveWord.get()
    }

    fun findLegalWordById(legalWordId: UUID): LegalWord {
        val optionalLegalWord = legalWordRepository.findById(legalWordId)
        if (!optionalLegalWord.isPresent) {
            throw IllegalArgumentException("Legal Word ID: $legalWordId not found")
        }
        return optionalLegalWord.get()
    }


    fun findAllSensitiveWords(): Iterable<SensitiveWord> {
        return sensitiveWordRepository.findAll()
    }

    fun findAllLegalWords(): Iterable<LegalWord> {
        return legalWordRepository.findAll()
    }

    fun addSensitiveWord(content: String): SensitiveWord {

        if (sensitiveWordRepository.existsByContent(content)) {
            throw IllegalArgumentException("${content} already found in existing sensitive words")
        }

        val word = SensitiveWord(content)


        sensitiveWordRepository.save(word)
        return word
    }

    fun saveLegalWords(sensitiveWordId: UUID, contents: Iterable<String>): Iterable<LegalWord> {

        val contentSet = contents.toSet()

        if (contentSet.size != contents.toList().size) {
            throw IllegalArgumentException("Original contents: $contents contain duplicates")
        }


        val sensitiveWord = findSensitiveWordById(sensitiveWordId)
        val deletedWordId = sensitiveWord.legalWords.map { it.id }



        val updatedLegalWords = contents.mapIndexed {
            index: Int, content: String ->  LegalWord(sequence = index, content= content)
        }

        updatedLegalWords.forEach {
            word -> word.sensitiveWord = sensitiveWord
        }

        sensitiveWord.legalWords = updatedLegalWords.toMutableList()
        sensitiveWordRepository.save(sensitiveWord)

        legalWordRepository.deleteAllById(deletedWordId)
        logger.info("Delete legal words: $deletedWordId")


        return sensitiveWord.legalWords
    }

    fun updateSensitiveWordContent(sensitiveWordId: UUID, content: String): SensitiveWord {
        val sensitiveWordFound = findSensitiveWordById(sensitiveWordId)
        sensitiveWordFound.content = content
        sensitiveWordRepository.save(sensitiveWordFound)

        return sensitiveWordFound
    }

    fun updateLegalWordContent(legalWordId: UUID,content: String): LegalWord {
        val legalWordFound = findLegalWordById(legalWordId)
        legalWordFound.content = content
        legalWordRepository.save(legalWordFound)
        return legalWordFound
    }

    fun deleteSensitiveWordByIds(ids: Iterable<UUID>) {
        return sensitiveWordRepository.deleteAllById(ids)
    }

    fun deleteLegalWordByIds(ids: Iterable<UUID>) {
        return legalWordRepository.deleteAllById(ids)
    }

    fun encode(content: String, member: Member): ArrayList<EncoderView> {
        val sensitiveWordViews = sensitiveWordRepository.findAll().map { it.toView() }
        return Encoder(sensitiveWordViews).encode(content)
    }

}