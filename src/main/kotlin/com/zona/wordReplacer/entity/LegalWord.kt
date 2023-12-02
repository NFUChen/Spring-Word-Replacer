package com.zona.wordReplacer.entity

import jakarta.persistence.*
import java.util.*


data class LegalWordView(
    val content: String,
    val id: UUID?
)

@Entity
@Table(name = "legal_word", uniqueConstraints = [
    UniqueConstraint(columnNames = ["sensitive_word_id", "content"])
])
class LegalWord(
    @Column(name = "content", nullable = false, length = 255)
    var content: String,

    @ManyToOne(targetEntity = SensitiveWord::class)
    @JoinColumn(name = "sensitive_word_id", referencedColumnName = "id", nullable = false)
    var sensitiveWord: SensitiveWord? = null,

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
) {
    fun toView(): LegalWordView {
        return LegalWordView(content, id)
    }
}


