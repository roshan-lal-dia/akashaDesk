package desk.akasha.core.application

import desk.akasha.core.domain.PublishRequest
import desk.akasha.core.domain.PublishResult
import desk.akasha.core.ports.PostPublisher

class PublishPostUseCase(
    private val postPublisher: PostPublisher,
) {
    suspend fun publish(request: PublishRequest): PublishResult {
        val normalized = request.copy(
            title = request.title?.trim()?.takeIf { it.isNotBlank() },
            markdown = request.markdown.trim(),
            tags = request.tags.map { it.trim() }.filter { it.isNotBlank() }.distinct(),
        )
        return postPublisher.publish(normalized)
    }
}

