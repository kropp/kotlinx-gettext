/*
 * Copyright 2022 Victor Kropp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.kropp.kotlinx.gettext

import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Single entry of [PoFile]
 */
data class PoEntry(
    /**
     * Translator comments
     */
    val comments: List<String>,
    /**
     * Comments extracted from the source code
     */
    val extractedComments: List<String>,
    /**
     * Reference to the source code where this entry has been extracted from
     */
    val references: List<String>,
    /**
     * Flags
     */
    val flags: String?,
    /**
     * Previous entries of the same entry
     */
    val previous: List<String>,
    /**
     * Contextual information
     */
    val context: String?,
    /**
     * Original message, usually an untranslated string
     */
    val text: String,
    /**
     * Plural form of original untranslated string
     */
    val plural: String?,
    /**
     * Different cases of translated string for different plural forms
     */
    val cases: List<String>
) {
    fun write(out: PrintStream) {
        for (comment in comments) {
            out.println("# $comment")
        }
        for (comment in extractedComments) {
            out.println("#. $comment")
        }
        for (reference in references) {
            out.println("#: $reference")
        }
        if (flags != null) {
            out.println("#, $flags")
        }
        for (prev in previous) {
            out.println("#| $prev")
        }
        if (context != null) {
            out.println("msgctxt \"${context.escape()}\"")
        }
        out.println("msgid \"${text.escape()}\"")
        if (plural != null) {
            out.println("msgid_plural \"${plural.escape()}\"")
            cases.forEachIndexed { idx, msg ->
                out.println("msgstr[$idx] \"${msg.escape()}\"")
            }
        } else {
            if (cases.isEmpty()) {
                out.println("msgstr \"\"")
            } else {
                out.println("msgstr \"${cases[0].escape()}\"")
            }
        }
    }

    private fun String.escape(): String {
        if (length <= 2) return this
        // replace " with \" when in the middle of a string
        return substring(0, this.lastIndex).replace(quoteRegex) { println(it.groupValues[1]); it.groupValues[1] + "\\\"" + it.groupValues[2] } + last()
    }
    private val quoteRegex = Regex("([^\n])\"(.)")

    override fun toString(): String {
        return ByteArrayOutputStream().use {
            PrintStream(it, false, Charsets.UTF_8).use { printer -> write(printer) }
            it.toString()
        }
    }
}