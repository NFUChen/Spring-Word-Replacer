package com.zona.wordReplacer.service
import com.zona.wordReplacer.entity.encoder.LegalWordView
import com.zona.wordReplacer.entity.encoder.SensitiveWordView

class EncoderView(
    val content: String,
    val isSensitiveWord: Boolean,
    val legalWords: ArrayList<LegalWordView> = arrayListOf()
)
data class EncoderRequest(
    val content: String
)

class Encoder(
    val sensitiveWordViews: Iterable<SensitiveWordView>
) {
    var map: MutableMap<String, ArrayList<LegalWordView>> = getMapFromViews()
    var allSensitiveWords = map.keys

    private fun getMapFromViews(): MutableMap<String, ArrayList<LegalWordView>> {
        val map: MutableMap<String, ArrayList<LegalWordView>> = mutableMapOf()
        for (view in sensitiveWordViews) {
            map[view.content] = ArrayList(view.legalWords.toList())
        }
        return map
    }

    fun encode(content: String): ArrayList<EncoderView> {
        var encodedContent = content
        for (word in allSensitiveWords) {
            encodedContent =  encodedContent.replace(word, "|${word}|")
        }

        val seperatedWords =  encodedContent.split("|").filter { it.length != 0 }
        var views: ArrayList<EncoderView> = arrayListOf()
        for (word in seperatedWords) {
            var legalWords = if (word in allSensitiveWords) map[word]?: arrayListOf() else arrayListOf()
            var isSensitive = word in allSensitiveWords
            views.add(
                EncoderView(word, isSensitive, legalWords)
            )
        }
        return views
    }






}