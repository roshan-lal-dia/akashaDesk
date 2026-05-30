package desk.akasha.core.api

import desk.akasha.core.application.PublishPostUseCase
import desk.akasha.core.domain.PublishRequest
import desk.akasha.core.infra.BetterAuthTokenVerifier
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.publishRoutes(
    tokenVerifier: BetterAuthTokenVerifier,
    publishPost: PublishPostUseCase,
) {
    post("/v1/publish") {
        val authorization = call.request.headers[HttpHeaders.Authorization]
        if (!tokenVerifier.accepts(authorization)) {
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "missing_or_invalid_bearer_token"))
            return@post
        }

        val request = call.receive<PublishRequest>()
        if (request.markdown.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "markdown_required"))
            return@post
        }

        val result = publishPost.publish(request)
        call.respond(HttpStatusCode.Accepted, result)
    }
}

