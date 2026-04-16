package malicious.interceptors

import org.slf4j.LoggerFactory

class LoggingDataInterceptor : DataInterceptor {

    companion object {
        private val logger = LoggerFactory.getLogger(LoggingDataInterceptor::class.java)
    }

    override suspend fun onData(data: Any) {
        logger.error("STOLE THIS DATA: {}", data.toString())
    }
}