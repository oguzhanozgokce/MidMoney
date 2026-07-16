package app.oguzhanozgokce.midmoney.network.interceptor

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.math.roundToInt

private const val MAX_BODY_BYTES = 8_192L
private const val NS_PER_MS = 1_000_000.0
private const val REDACTED_QUERY = "token"

/**
 * Logs each call as a single compact block instead of OkHttp's header-per-line output:
 *
 * ```
 * → GET /quote?symbol=AAPL
 * ← 200 (558 ms)
 * { "c": 150.25, ... }
 * ```
 *
 * The API token query parameter is redacted so it never lands in Logcat. Debug builds only.
 */
class NetworkLoggingInterceptor(
    private val json: Json,
    private val log: (String) -> Unit,
) : Interceptor {

    private val prettyJson = Json(from = json) { prettyPrint = true }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val safeUrl = request.url.newBuilder().removeAllQueryParameters(REDACTED_QUERY).build()
        val path = safeUrl.encodedPath + (safeUrl.encodedQuery?.let { "?$it" }.orEmpty())

        val startNs = System.nanoTime()
        val response = try {
            chain.proceed(request)
        } catch (error: IOException) {
            log("→ ${request.method} $path\n← FAILED (${error.message})")
            throw error
        }
        val tookMs = ((System.nanoTime() - startNs) / NS_PER_MS).roundToInt()

        val body = runCatching { response.peekBody(MAX_BODY_BYTES).string() }.getOrNull().orEmpty()
        val message = buildString {
            append("→ ${request.method} $path\n")
            append("← ${response.code} ($tookMs ms)")
            prettify(body).takeIf { it.isNotBlank() }?.let { append("\n").append(it) }
        }
        log(message)
        return response
    }

    private fun prettify(body: String): String {
        if (body.isBlank()) return ""
        return runCatching {
            prettyJson.encodeToString(JsonElement.serializer(), json.parseToJsonElement(body))
        }.getOrDefault(body)
    }
}
