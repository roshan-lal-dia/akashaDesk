package desk.akasha.core.infra

class BetterAuthTokenVerifier(
    private val developmentToken: String? = System.getenv("CORE_DEV_BEARER_TOKEN"),
) {
    fun accepts(authorizationHeader: String?): Boolean {
        val token = authorizationHeader
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.trim()
            ?: return false

        val configuredDevelopmentToken = developmentToken?.takeIf { it.isNotBlank() } ?: return false
        return token == configuredDevelopmentToken
    }
}
