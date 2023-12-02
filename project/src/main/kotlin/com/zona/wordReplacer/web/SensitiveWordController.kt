package com.zona.wordReplacer.web

import com.zona.wordReplacer.entity.SensitiveWord
import com.zona.wordReplacer.entity.SensitiveWordView
import com.zona.wordReplacer.service.EncoderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/sensitive_words")
class SensitiveWordController(
    val encoderService: EncoderService
) {
    @GetMapping("/view")
    fun getView(): Response<Iterable<SensitiveWordView>> {
        return Response(encoderService.findAllSensitiveWords().map { it.toView() })
    }

    @GetMapping("/")
    fun get(): Response<Iterable<SensitiveWord>> {
        return Response(encoderService.findAllSensitiveWords())
    }

    @PostMapping("/")
    fun post(@RequestBody sensitiveWord: SensitiveWord): Response<SensitiveWord> {
        encoderService.addSensitiveWord(sensitiveWord)
        return Response(sensitiveWord)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: UUID, @RequestBody sensitiveWord: SensitiveWord): Response<SensitiveWord> {
        val updatedWord = encoderService.updateSensitiveWordContent(id, sensitiveWord.content)
        return Response(updatedWord)
    }

    @DeleteMapping("/")
    fun delete(@RequestBody ids: Iterable<UUID>): Response<String> {
        encoderService.deleteSensitiveWordByIds(ids)
        return Response("Successfully delete sensitive words: $ids", HttpStatus.ACCEPTED)
    }
}