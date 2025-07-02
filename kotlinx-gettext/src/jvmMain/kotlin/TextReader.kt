package name.kropp.kotlinx.gettext

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * JVM implementation of TextReader that wraps an InputStream.
 */
internal class InputStreamReader(inputStream: InputStream) : Reader {
    private val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

    override fun readLine(): String? = reader.readLine()

    override fun close() {
        reader.close()
    }
}

/**
 * Extension function to create a TextReader from an InputStream.
 */
public fun InputStream.asTextReader(): Reader = name.kropp.kotlinx.gettext.InputStreamReader(this)