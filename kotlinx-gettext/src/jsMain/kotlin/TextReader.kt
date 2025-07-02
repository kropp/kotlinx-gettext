package name.kropp.kotlinx.gettext

/**
 * JS implementation of TextReader that reads from a string.
 */
internal class StringReader(content: String) : Reader {
    private val lines = content.split("\n").toMutableList()
    private var currentIndex = 0

    override fun readLine(): String? {
        if (currentIndex >= lines.size) {
            return null
        }
        return lines[currentIndex++]
    }

    override fun close() {
        // No resources to close
    }
}

/**
 * Creates a TextReader from a file path.
 * This is a simplified version that works for tests.
 */
@JsModule("fs")
@JsNonModule
private external object NodeFS {
    fun readFileSync(path: String, options: dynamic = definedExternally): String
}

/**
 * Creates a TextReader from a file path.
 */
internal fun readTextFile(path: String): Reader {
    val content = NodeFS.readFileSync(path, js("{ encoding: 'utf8' }"))
    return StringReader(content)
}