package com.zona.wordReplacer.service

import com.zona.wordReplacer.entity.encoder.LegalWord
import com.zona.wordReplacer.entity.encoder.LegalWordView
import com.zona.wordReplacer.entity.encoder.SensitiveWord
import com.zona.wordReplacer.repository.LegalWordRepository
import com.zona.wordReplacer.repository.SensitiveWordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class EncoderService(
    val sensitiveWordRepository: SensitiveWordRepository,
    val legalWordRepository: LegalWordRepository
) {
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
        val word = SensitiveWord(content)
        sensitiveWordRepository.save(word)
        return word
    }

    fun addLegalWords(sensitiveWordId: UUID, contents: Iterable<String>): Iterable<LegalWord> {
        // checking duplicates


        val sensitiveWord = findSensitiveWordById(sensitiveWordId)
        val legalWords = contents.mapIndexed {
            index: Int, content: String ->  LegalWord(sequence = index, content= content)
        }

        legalWords.forEach {
            word -> word.sensitiveWord = sensitiveWord
        }
        legalWordRepository.saveAll(legalWords)
        return legalWords
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

    fun updateLegalWords(sensitiveWordId: UUID,legalWordViews: Iterable<LegalWordView>): Iterable<LegalWord> {
        val optionalSensitiveWord = sensitiveWordRepository.findById(sensitiveWordId)
        if (optionalSensitiveWord.isEmpty) {
            throw IllegalArgumentException("Unable to find sensitive word: ${sensitiveWordId}")
        }
        val sensitiveWord = optionalSensitiveWord.get()

        val existedWords = legalWordRepository.findAllById(legalWordViews.map { it.id })
        if (existedWords.toSet().size != legalWordViews.toSet().size) {
            throw IllegalArgumentException("Some elements in missing in existing words")
        }




        legalWordRepository.deleteAll(existedWords)

        val words = legalWordViews.mapIndexed{
            sequence, legalWordView -> LegalWord(
            sequence = sequence,
            content = legalWordView.content,
            sensitiveWord=optionalSensitiveWord.get()
            )
        }
        val updatedWords = legalWordRepository.saveAll(words)


        return updatedWords
    }

    fun deleteSensitiveWordByIds(ids: Iterable<UUID>) {
        return sensitiveWordRepository.deleteAllById(ids)
    }

    fun deleteLegalWordByIds(ids: Iterable<UUID>) {
        return legalWordRepository.deleteAllById(ids)
    }

    fun encode(content: String): ArrayList<EncoderView> {
        val sensitiveWordViews = sensitiveWordRepository.findAll().map { it.toView() }
        return Encoder(sensitiveWordViews).encode(content)
    }

}