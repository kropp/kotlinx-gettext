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

package com.github.kropp.kotlinx.gettext

import java.io.*

/**
 * Gettext .po (Portable Object) & .pot (PO Template) files support
 */
class PoFile(
    val entries: List<PoEntry>,
    val header: PoEntry = DEFAULT_POT_HEADER
) {
    fun write(out: OutputStream) {
        PrintStream(out, false, Charsets.UTF_8).use { writer ->
            writer.println(header)
            for (message in entries) {
                writer.println()
                message.write(writer)
            }
        }
    }

    companion object {
        @JvmStatic
        fun fromUnmerged(messages: List<PoEntry>): PoFile {
            val merged =
                messages
                    .groupBy { it.text }
                    .map { group ->
                        group.value.first().copy(references = group.value.flatMap { it.references })
                    }
            return PoFile(merged)
        }

        @JvmStatic
        fun read(input: InputStream): PoFile {
            val entries = mutableListOf<PoEntry>()

            var comments = mutableListOf<String>()
            var extractedComments = mutableListOf<String>()
            var references = mutableListOf<String>()
            var flags: String? = null
            var previous = mutableListOf<String>()
            var context: String? = null
            var text: String? = null
            var plural: String? = null
            var cases = mutableListOf<String>()

            input.bufferedReader(Charsets.UTF_8).useLines { lines ->
                lines.forEach { line ->
                    when {
                        line.isBlank() -> {
                            text?.let {
                                entries += PoEntry(comments, extractedComments, references, flags, previous, context, it, plural, cases)
                            }
                            comments = mutableListOf()
                            extractedComments = mutableListOf()
                            references = mutableListOf()
                            flags = null
                            previous = mutableListOf()
                            context = null
                            text = null
                            plural = null
                            cases = mutableListOf()
                        }
                        line.startsWith("#  ") -> comments += line.substringAfter("#  ")
                        line.startsWith("#. ") -> extractedComments += line.substringAfter("#. ")
                        line.startsWith("#: ") -> references += line.substringAfter("#: ")
                        line.startsWith("#, ") -> flags = line.substringAfter("#, ")
                        line.startsWith("#| ") -> previous += line.substringAfter("#| ")
                        line.startsWith("msgctxt ") -> context = line.substringAfter("msgctx ").trim('"')
                        line.startsWith("msgid ") -> text = line.substringAfter("msgid ").trim('"')
                        line.startsWith("msgid_plural ") -> plural = line.substringAfter("msgid_plural ").trim('"')
                        line.startsWith("msgstr ") -> cases += line.substringAfter("msgstr ").trim('"')
                        line.startsWith("msgstr[") -> cases += line.substringAfter("msgstr[").substringAfter(']').trim('"')
                        else -> {
                            if (cases.isNotEmpty()) {
                                cases[0] = cases[0] + "\"\n\"" + line.trim('"')
                            }
                        }
                    }
                }
                text?.let {
                    entries += PoEntry(comments, extractedComments, references, flags, previous, context, it, plural, cases)
                }
            }

            return PoFile(entries.filter { it.text.isNotEmpty() }, entries.firstOrNull { it.text.isEmpty() } ?: DEFAULT_POT_HEADER)
        }
    }
}

val DEFAULT_POT_HEADER = PoEntry(
    listOf(
        "SOME DESCRIPTIVE TITLE.",
        "Copyright (C) YEAR THE PACKAGE'S COPYRIGHT HOLDER",
        "This file is distributed under the same license as the PACKAGE package.",
        "FIRST AUTHOR <EMAIL@ADDRESS>, YEAR.",
        ""
    ),
    emptyList(),
    emptyList(),
    "fuzzy",
    emptyList(),
    null,
    "",
    null,
    listOf(
        "\"\n\""+
        "Project-Id-Version: PACKAGE VERSION\\n\"\n\""+
        "Report-Msgid-Bugs-To: \\n\"\n\""+
        "\"\n\""+
        "PO-Revision-Date: YEAR-MO-DA HO:MI+ZONE\\n\"\n\""+
        "Last-Translator: FULL NAME <EMAIL@ADDRESS>\\n\"\n\""+
        "Language-Team: LANGUAGE <LL@li.org>\\n\"\n\""+
        "Language: \\n\"\n\""+
        "MIME-Version: 1.0\\n\"\n\""+
        "Content-Type: text/plain; charset=UTF-8\\n\"\n\""+
        "Content-Transfer-Encoding: 8bit\\n\"\n\""+
        "Plural-Forms: nplurals=INTEGER; plural=EXPRESSION;\\n"
    )
)