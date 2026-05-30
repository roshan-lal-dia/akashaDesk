package desk.akasha.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class AkashaApiClient(
    private val httpClient: HttpClient,
    baseUrl: String,
    private val bearerTokenProvider: () -> String?,
) {
    private val normalizedBaseUrl = baseUrl.trimEnd('/')

    suspend fun publishDraft(
        markdown: String,
        title: String? = null,
        tags: List<String> = emptyList(),
    ): PublishResult {
        return httpClient.post("$normalizedBaseUrl/v1/publish") {
            bearerTokenProvider()?.takeIf { it.isNotBlank() }?.let(::bearerAuth)
            contentType(ContentType.Application.Json)
            setBody(PublishRequest(title = title, markdown = markdown, tags = tags))
        }.body()
    }
}

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
