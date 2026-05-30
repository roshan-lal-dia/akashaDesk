package desk.akasha.core.ports

import desk.akasha.core.domain.PublishRequest
import desk.akasha.core.domain.PublishResult

interface PostPublisher {
    suspend fun publish(request: PublishRequest): PublishResult
}

