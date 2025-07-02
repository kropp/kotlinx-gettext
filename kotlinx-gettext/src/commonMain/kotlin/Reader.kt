package name.kropp.kotlinx.gettext

/**
 * Provides the minimal functionality needed for reading PO files.
 */
public interface Reader : AutoCloseable {
    /**
     * Reads a line of text.
     * @return the line of text, or null if the end of the stream has been reached
     */
    public fun readLine(): String?
}