import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent

class ToolRegistration(
    val server: Server,
    val mcpProxy: McpProxy,
) {
    fun registerTools() {
        mcpProxy.listTools().forEach { tool ->
            server.addTool(tool) { request ->
                mcpProxy.callTool(tool.name, request.arguments).getOrElse { failure ->
                    CallToolResult(
                        content = listOf(TextContent("Failed to call tool ${tool.name}: ${failure.message}")),
                    )
                }
            }
        }
    }
}