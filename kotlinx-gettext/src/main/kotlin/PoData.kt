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

import java.io.InputStream

/**
 * Low-level representation of PO file.
 * Clients should use [I18n] instead.
 */
class PoData(
    private val strings: Map<String, PoEntry>,
    private val pluralRule: PluralRuleExpression
) {
    /**
     * Get translation string for the key [id].
     * Note: When context is needed, the key is prefixed with it and [CONTEXT_DELIMITER].
     */
    operator fun get(id: String): String? {
        return strings[id]?.str
    }

    /**
     * Get plural translation for the key [id]. The plural form is evaluated against the number [n].
     * Note: When context is needed, the key is prefixed with it and [CONTEXT_DELIMITER].
     */
    operator fun get(id: String, n: Int): String? {
        val entry = strings[id] ?: return null
        val case = pluralRule.evaluate(n)
        if (0 <= case && case < entry.cases.size) {
            return entry.cases[case]
        }
        return entry.plural
    }

    private enum class Key {
        Context,
        Id,
        Str,
        Plural,
        Cases,
        Unknown
    }

    companion object {
        /**
         * Context and key separator.
         */
        @JvmStatic
        val CONTEXT_DELIMITER = '\u0004'

        /**
         * Loads PO file from provided [input].
         */
        @JvmStatic
        fun read(input: InputStream): PoData {
            val entries = mutableMapOf<String, PoEntry>()
            var pluralRule = ""

            var context: String? = null
            var id: String? = null
            var str = ""
            var plural: String? = null
            var cases = mutableListOf<String>()

            var key = Key.Unknown

            input.bufferedReader(Charsets.UTF_8).useLines { lines ->
                lines.forEach {
                    val line = it.replace("\\n", "\n")
                    when {
                        line.isBlank() -> {
                            entry(entries, context, id, str, plural, cases)
                            context = null
                            id = null
                            str = ""
                            plural = null
                            cases = mutableListOf()
                            key = Key.Unknown
                        }
                        line.startsWith("msgctxt ") -> { context = line.substringAfter("msgctxt ").trim('"'); key = Key.Context }
                        line.startsWith("msgid ") -> { id = line.substringAfter("msgid ").trim('"'); key = Key.Id }
                        line.startsWith("msgid_plural ") -> { plural = line.substringAfter("msgid_plural ").trim('"'); key = Key.Plural }
                        line.startsWith("msgstr ") -> { str = line.substringAfter("msgstr ").trim('"'); key = Key.Str }
                        line.startsWith("msgstr[") -> { cases += line.substringAfter("msgstr[").substringAfter("] ").trim('"'); key = Key.Cases }
                        else -> {
                            val trimmed = line.trim('"')
                            if (id?.isEmpty() == true && key == Key.Str && trimmed.startsWith("Plural-Forms:")) {
                                pluralRule = trimmed.substringAfter("plural=").substringBefore(';')
                            } else when(key) {
                                Key.Context -> context += trimmed
                                Key.Id -> id += trimmed
                                Key.Str -> str += trimmed
                                Key.Plural -> plural += trimmed
                                Key.Cases -> if (cases.isNotEmpty()) {
                                    cases[cases.size - 1] += trimmed
                                }
                                else -> {}
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

