package com.zona.wordReplacer.web.controller

import com.zona.wordReplacer.entity.encoder.LegalWord
import com.zona.wordReplacer.entity.encoder.LegalWordView
import com.zona.wordReplacer.service.EncoderService
import com.zona.wordReplacer.web.Response
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/admin/legal_words")
class LegalWordController(
    val encoderService: EncoderService
) {
    @GetMapping("/view")
    fun getView(): Response<Iterable<LegalWordView>> {
        return Response(encoderService.findAllLegalWords().map { it.toView() })
    }

    @GetMapping
    fun get(): Response<Iterable<LegalWord>> {
        return Response(encoderService.findAllLegalWords())
    }


    @PostMapping("/{id}")
    fun post(@PathVariable id: UUID,@RequestBody contents: Iterable<String>): Response<Iterable<LegalWord>> {
        val createdWord = encoderService.saveLegalWords(id, contents)
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