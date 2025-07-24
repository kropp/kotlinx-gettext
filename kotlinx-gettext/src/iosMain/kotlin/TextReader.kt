package name.kropp.kotlinx.gettext

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

/**
 * iOS implementation of TextReader that reads from a string.
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
 */
@ExperimentalForeignApi
internal fun readTextFile(path: String): Reader {
    val content = NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null) ?: ""
    return StringReader(content)
}
