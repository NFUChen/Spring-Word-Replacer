package com.zona.wordReplacer.entity.encoder

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID


data class SensitiveWordView(
    val id: UUID?,
    val content: String,
    val legalWords: Iterable<LegalWordView>
)


@Entity
@Table(name = "sensitive_word")
class SensitiveWord(
    @Column(name = "content", nullable = false, unique = true, length = 255)
    var content: String,

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @JsonIgnore
    @OneToMany(targetEntity = LegalWord::class, cascade = [CascadeType.ALL], mappedBy = "sensitiveWord")
    var legalWords: MutableList<LegalWord> = mutableListOf()
) {
    fun toView(): SensitiveWordView {
        return SensitiveWordView(
            id,
            content,
            legalWords.map { word -> word.toView() }
        )
    }
}