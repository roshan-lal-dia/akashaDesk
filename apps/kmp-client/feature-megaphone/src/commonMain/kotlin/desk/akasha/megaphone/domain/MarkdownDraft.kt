package desk.akasha.megaphone.domain

data class MarkdownDraft(
    val title: String,
    val markdown: String,
    val tags: List<String> = emptyList(),
    val localImageReferences: List<LocalImageReference> = emptyList(),
)

data class LocalImageReference(
    val absolutePath: String,
    val altText: String,
)

data class DraftOutline(
    val headings: List<String>,
    val wordCount: Int,
)

class DraftAnalyzer {
    fun analyze(markdown: String): DraftOutline {
        val headings = markdown
            .lineSequence()
            .filter { it.startsWith("#") }
            .map { it.trimStart('#').trim() }
            .filter { it.isNotEmpty() }
            .toList()
        val wordCount = markdown
            .split(Regex("\\s+"))
            .count { it.isNotBlank() }
        return DraftOutline(headings = headings, wordCount = wordCount)
    }
}

