package com.zona.wordReplacer.web

import com.zona.wordReplacer.repository.SensitiveWordRepository
import com.zona.wordReplacer.service.EncoderRequest
import com.zona.wordReplacer.service.EncoderService
import com.zona.wordReplacer.service.EncoderView
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/encoder")
class EncoderController(
    val encoderService: EncoderService
) {
    @PostMapping("/encode")
    fun encode(@RequestBody encoderRequest: EncoderRequest): Response<ArrayList<EncoderView>> {
        return Response(encoderService.encode(encoderRequest.content))
    }
}