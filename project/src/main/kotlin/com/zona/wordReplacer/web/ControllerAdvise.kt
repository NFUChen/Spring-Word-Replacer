package com.zona.wordReplacer.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ControllerAdvise {

    @ExceptionHandler
    fun handleException(exception: Exception): Response<String> {
        return Response(exception.message?: "", HttpStatus.BAD_REQUEST)
    }
}