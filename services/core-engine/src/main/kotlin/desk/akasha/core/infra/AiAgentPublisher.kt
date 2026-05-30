package desk.akasha.core.infra

import desk.akasha.core.domain.PublishRequest
import desk.akasha.core.domain.PublishResult
import desk.akasha.core.ports.PostPublisher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AiAgentPublisher(
    private val client: HttpClient,
    baseUrl: String,
    private val internalToken: String?,
) : PostPublisher {
    private val normalizedBaseUrl = baseUrl.trimEnd('/')

    override suspend fun publish(request: PublishRequest): PublishResult {
        return client.post("$normalizedBaseUrl/internal/publish") {
            internalToken?.takeIf { it.isNotBlank() }?.let {
                header("X-Internal-Token", it)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
