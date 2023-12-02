package com.zona.wordReplacer.web

import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class Response <T> (
    val data: T,
    val status: HttpStatus = HttpStatus.OK,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
}