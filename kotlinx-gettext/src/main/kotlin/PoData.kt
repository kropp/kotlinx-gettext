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

import java.io.InputStream

class PoData(
    private val strings: Map<String, PoEntry>,
    private val pluralRule: PluralRuleExpression
) {
    operator fun get(id: String): String? {
        return strings[id]?.str
    }

    operator fun get(id: String, n: Int): String? {
        val entry = strings[id] ?: return null
        val case = pluralRule.evaluate(n)
        if (0 <= case && case < entry.cases.size) {
            return entry.cases[case]
        }
        return entry.plural
    }

    companion object {
        @JvmStatic
        val CONTEXT_DELIMITER = '\u0004'

        @JvmStatic
        fun read(input: InputStream): PoData {
            val entries = mutableMapOf<String, PoEntry>()
            var pluralRule = ""

            var context: String? = null
            var id: String? = null
            var str = ""
            var plural: String? = null
            var cases = mutableListOf<String>()

            input.bufferedReader(Charsets.UTF_8).useLines { lines ->
                lines.forEach { line ->
                    when {
                        line.isBlank() -> {
                            entry(entries, context, id, str, plural, cases)
                            context = null
                            id = null
                            str = ""
                            plural = null
                            cases = mutableListOf()
                        }
                        line.startsWith("msgctxt ") -> context = line.substringAfter("msgctxt ").trim('"')
                        line.startsWith("msgid ") -> id = line.substringAfter("msgid ").trim('"')
                        line.startsWith("msgid_plural ") -> plural = line.substringAfter("msgid_plural ").trim('"')
                        line.startsWith("msgstr ") -> str = line.substringAfter("msgstr ").trim('"')
                        line.startsWith("msgstr[") -> cases += line.substringAfter("msgstr[").substringAfter("] ").trim('"')
                        else -> {
                            val trimmed = line.trim('"')
                            if (id?.isEmpty() == true && trimmed.startsWith("Plural-Forms:")) {
                                pluralRule = trimmed.substringAfter("plural=").substringBefore(';')
                            }
                            if (cases.isNotEmpty()) {
                                cases[0] = cases[0] + "\"\n\"" + trimmed
                            }
                        }
                    }
                }
                entry(entries, context, id, str, plural, cases)
            }
            return PoData(entries, PluralRule(pluralRule))
        }

        @JvmStatic
        private fun entry(entries: MutableMap<String, PoEntry>, context: String?, id: String?, str: String, plural: String?, cases: List<String>) {
            if (!id.isNullOrEmpty()) {
                entries[if (context != null) "$context$CONTEXT_DELIMITER$id" else id] = PoEntry(str, plural, cases.toTypedArray())
            }
        }
    }
}

/**
 * Data for a single PO file entry.
 */
class PoEntry(
    val str: String,
    val plural: String?,
    val cases: Array<String>
)
