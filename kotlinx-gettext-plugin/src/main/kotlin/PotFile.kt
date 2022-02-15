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
import java.nio.charset.Charset

class PotFile(
    private val messages: List<MsgId>,
) {
    fun generate(out: OutputStream) {
        PrintStream(out, false, Charset.forName("UTF-8")).use { writer ->
            writer.generateHeader()
            writer.generateMessages()
        }
    }

    private fun PrintStream.generateMessages() {
        for (message in messages) {
            println()
            for (reference in message.references) {
                println("#: $reference")
            }
            println("msgid \"${message.text}\"")
            if (message.plural != null) {
                println("msgid_plural \"${message.plural}\"")
            }
            println("msgstr \"\"")
        }
    }

    private fun PrintStream.generateHeader() {
        println("""
            # SOME DESCRIPTIVE TITLE.
            # Copyright (C) YEAR THE PACKAGE'S COPYRIGHT HOLDER
            # This file is distributed under the same license as the PACKAGE package.
            # FIRST AUTHOR <EMAIL@ADDRESS>, YEAR.
            #
            #, fuzzy
            msgid ""
            msgstr ""
            "Project-Id-Version: PACKAGE VERSION\n"
            "Report-Msgid-Bugs-To: \n"
            ""
            "PO-Revision-Date: YEAR-MO-DA HO:MI+ZONE\n"
            "Last-Translator: FULL NAME <EMAIL@ADDRESS>\n"
            "Language-Team: LANGUAGE <LL@li.org>\n"
            "Language: \n"
            "MIME-Version: 1.0\n"
            "Content-Type: text/plain; charset=UTF-8\n"
            "Content-Transfer-Encoding: 8bit\n"
            "Plural-Forms: nplurals=INTEGER; plural=EXPRESSION;\n"
        """.trimIndent())
    }

    companion object {
        fun fromUnmerged(messages: List<MsgId>): PotFile {
            val merged =
                messages
                    .groupBy { it.text }
                    .map { group ->
                        group.value.first().copy(references = group.value.flatMap { it.references })
                    }
            return PotFile(merged)
        }
    }
}