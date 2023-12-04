package com.zona.wordReplacer.service

import com.zona.wordReplacer.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class AuthService(
    val encoder: BCryptPasswordEncoder,
    val memberRepository: MemberRepository,
){
    val logger = LoggerFactory.getLogger(this::class.java.name)
    val KEY = "JSID"
    val sessionPool = HashMap<String, Long>()
    val MaxSize = 1000

    fun encodePassword(password: String): String {
        return encoder.encode(password)
    }

    private fun isValidPassword(rawPassword: String, encodedPassword: String): Boolean {
        return encoder.matches(rawPassword, encodedPassword)
    }

    private fun addSessionId(sid: String, memberId: Long) {
        if (sessionPool.size > MaxSize) {
            val key = sessionPool.keys.elementAt(0)
            sessionPool.remove(key)
            logger.warn("Exceed session pool size: pop element at index of 0 for more spaces")
        }
        logger.info("SET SID: $sid TO USER $memberId")
        sessionPool[sid] = memberId
    }

    fun login(email: String, password: String): String {
        val optionalMember = memberRepository.findByEmail(email)
        if (optionalMember.isEmpty) {
            throw IllegalArgumentException(
                "Email: $email not found"
            )
        }
        val member = optionalMember.get()

        val encodedPassword = member.password ?: ""


        if (!isValidPassword(password, encodedPassword)) {
           throw IllegalArgumentException("BAD CREDENTIALS")
        }
        val sid =  UUID.randomUUID().toString()

        addSessionId(sid, member.id!!)
        return sid
    }

    fun logout(sessionId: String) {
        sessionPool.remove(sessionId)
    }



    fun isValidSessionId(sessionId: String): Boolean {
        return sessionPool.containsKey(sessionId)
    }
}