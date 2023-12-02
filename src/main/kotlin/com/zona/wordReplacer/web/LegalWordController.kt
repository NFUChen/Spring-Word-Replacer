package com.zona.wordReplacer.web

import com.zona.wordReplacer.entity.LegalWord
import com.zona.wordReplacer.entity.LegalWordView
import com.zona.wordReplacer.entity.SensitiveWord
import com.zona.wordReplacer.service.EncoderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/legal_words")
class LegalWordController(
    val encoderService: EncoderService
) {
    @GetMapping("/view")
    fun getView(): Response<Iterable<LegalWordView>> {
        return Response(encoderService.findAllLegalWords().map { it.toView() })
    }

    @GetMapping("/")
    fun get(): Response<Iterable<LegalWord>> {
        return Response(encoderService.findAllLegalWords())
    }



    @PostMapping("/{id}")
    fun post(@PathVariable id: UUID,@RequestBody legalWords: Iterable<LegalWord>): Response<Iterable<LegalWord>> {
        val createdWord = encoderService.addLegalWords(id, legalWords )
        return Response(createdWord)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: UUID, @RequestBody legalWord: LegalWord): Response<LegalWord> {
        val updatedWord = encoderService.updateLegalWordContent(id, legalWord.content)
        return Response(updatedWord)
    }

    @DeleteMapping("/")
    fun delete(@RequestBody ids: Iterable<UUID>): Response<String> {
        encoderService.deleteLegalWordByIds(ids)
        return Response("Successfully delete legal words: $ids", HttpStatus.ACCEPTED)
    }
}