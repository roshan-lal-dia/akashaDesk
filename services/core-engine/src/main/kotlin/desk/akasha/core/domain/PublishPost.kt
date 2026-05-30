package desk.akasha.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class PublishRequest(
    val title: String? = null,
    val markdown: String,
    val tags: List<String> = emptyList(),
)

@Serializable
data class PublishResult(
    val status: PublishStatus,
    val slug: String,
    val seoTitle: String,
    val description: String,
    val targetPath: String,
    val commitSha: String? = null,
)

@Serializable
enum class PublishStatus {
    ACCEPTED,
    PUBLISHED,
    BLOCKED,
}

