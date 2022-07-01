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

import okio.Source

/**
 * Provides `gettext`-compatible interface for string translation via PO files.
 */
public class Gettext private constructor(
    override val locale: Locale,
    private val poData: PoData
) : I18n {
    override fun tr(text: String): String {
        return poData[text] ?: text
    }

    override fun tr(text: String, vararg args: Pair<String, String>): String =
        tr(text).format(args)

    override fun trn(text: String, plural: String, n: Int): String {
        return poData[text, n] ?: if (n == 1) text else plural
    }

    override fun trn(text: String, plural: String, n: Int, vararg args: Pair<String, String>): String =
        trn(text, plural, n).format(args)

    override fun trn(text: String, plural: String, n: Long): String = trn(text, plural, n.toInt())

    override fun trn(text: String, plural: String, n: Long, vararg args: Pair<String, String>): String =
        trn(text, plural, n).format(args)

    override fun trc(context: String, text: String): String {
        return poData["$context${PoData.CONTEXT_DELIMITER}$text"] ?: text
    }

    override fun trc(context: String, text: String, vararg args: Pair<String,String>): String =
        trc(context, text).format(args)

    override fun trnc(context: String, text: String, plural: String, n: Int): String {
        return poData["$context${PoData.CONTEXT_DELIMITER}$text", n] ?: if (n == 1) text else plural
    }

    override fun trnc(context: String, text: String, plural: String, n: Int, vararg args: Pair<String,String>): String =
        trnc(context, text, plural, n).format(args)

    override fun trnc(context: String, text: String, plural: String, n: Long): String =
        trnc(context, text, plural, n.toInt())

    override fun trnc(context: String, text: String, plural: String, n: Long, vararg args: Pair<String,String>): String =
        trnc(context, text, plural, n.toInt()).format(args)

    override fun marktr(text: String): String = text

    private fun String.format(args: Array<out Pair<String, String>>): String {
        var currentOffset = 0
        var nextIndex = indexOf("{{", currentOffset)
        if (nextIndex == -1) {
            return this
        }

        return buildString {
            do {
                append(this@format.substring(currentOffset, nextIndex))
                currentOffset = nextIndex + 2
                nextIndex = this@format.indexOf("}}", currentOffset)
                if (nextIndex == -1) {
                    // incorrect format, append the rest of the string as is
                    break
                }
                val key = this@format.substring(currentOffset, nextIndex)
                for (arg in args) {
                    if (key == arg.first) {
                        append(arg.second)
                        break
                    }
                }
                currentOffset = nextIndex + 2
                nextIndex = this@format.indexOf("{{", currentOffset)
            } while (nextIndex != -1)

            if (currentOffset < this@format.length) {
                append(this@format.substring(currentOffset, this@format.length))
            }
        }
    }

    public companion object {
        /**
         * Load translations for given [locale] from given input stream.
         */
        public fun load(locale: Locale, s: Source): Gettext {
            return Gettext(locale, PoData.read(s))
        }

        /**
         * Fallback locale that doesn't provide any translations.
         */
        public val Fallback: Gettext = Gettext(DefaultLocale, PoData(emptyMap(), EmptyRule))
    }
}