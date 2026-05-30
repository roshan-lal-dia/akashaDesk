package desk.akasha.core

import desk.akasha.core.api.publishRoutes
import desk.akasha.core.application.PublishPostUseCase
import desk.akasha.core.infra.AiAgentPublisher
import desk.akasha.core.infra.BetterAuthTokenVerifier
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

fun main() {
    embeddedServer(
        factory = Netty,
        host = "0.0.0.0",
        port = env("PORT", "8080").toInt(),
        module = Application::akashadeskModule,
    ).start(wait = true)
}

fun Application.akashadeskModule() {
    val jsonCodec = Json {
        ignoreUnknownKeys = true
    }

    install(ServerContentNegotiation) {
        json(jsonCodec)
    }

    val httpClient = HttpClient(CIO) {
        install(ClientContentNegotiation) {
            json(jsonCodec)
        }
    }
    monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }

    val publisher = AiAgentPublisher(
        client = httpClient,
        baseUrl = env("AI_SERVICE_URL", "http://ai-service:8000"),
        internalToken = System.getenv("AI_AGENT_INTERNAL_TOKEN"),
    )

    routing {
        get("/healthz") {
            call.respond(mapOf("status" to "ok", "service" to "core-engine"))
        }
        publishRoutes(
            tokenVerifier = BetterAuthTokenVerifier(),
            publishPost = PublishPostUseCase(publisher),
        )
    }
}

private fun env(name: String, defaultValue: String): String {
    return System.getenv(name) ?: defaultValue
}
