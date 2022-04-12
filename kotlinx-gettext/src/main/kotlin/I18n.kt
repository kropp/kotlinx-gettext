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
import java.text.MessageFormat
import java.util.*

/**
 * Provides `gettext`-compatible interface for string translation via PO files.
 */
class I18n private constructor(
    val locale: Locale,
    private val poData: PoData
) {
    /**
     * Translate [text].
     */
    fun tr(text: String): String {
        return poData[text] ?: text
    }

    /**
     * Translate [MessageFormat]-compatible formatted [text] with provided [args].
     */
    fun tr(text: String, vararg args: Any?): String {
        val str = poData[text] ?: text
        return MessageFormat.format(str, *args)
    }

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Int): String {
        return poData[text, n] ?: if (n == 1) text else plural
    }

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Long): String = trn(text, plural, n.toInt())

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Int, vararg args: Any?): String {
        val str = poData[text, n] ?: if (n == 1) text else plural
        return MessageFormat.format(str, *args)
    }

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Long, vararg args: Any?): String = trn(text, plural, n.toInt(), *args)

    /**
     * Translate [text] in a given [context].
     */
    fun trc(context: String, text: String): String {
        return poData["$context${PoData.CONTEXT_DELIMITER}$text"] ?: text
    }

    /**
     * Translate [MessageFormat]-compatible formatted [text] with provided [args] in a given [context].
     */
    fun trc(context: String, text: String, vararg args: Any?): String {
        val str = poData["$context${PoData.CONTEXT_DELIMITER}$text"] ?: text
        return MessageFormat.format(str, *args)
    }

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Int): String {
        return poData["$context${PoData.CONTEXT_DELIMITER}$text", n] ?: if (n == 1) text else plural
    }

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Long): String = trnc(context, text, plural, n.toInt())

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Int, vararg args: Any?): String {
        val str = poData["$context${PoData.CONTEXT_DELIMITER}$text", n] ?: if (n == 1) text else plural
        return MessageFormat.format(str, *args)
    }

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Long, vararg args: Any?): String =
        trnc(context, text, plural, n.toInt(), *args)

    companion object {
        /**
         * Load translations for given [locale] from given input stream.
         */
        @JvmStatic
        fun load(locale: Locale, s: InputStream): I18n {
            return I18n(locale, PoData.read(s))
        }

        /**
         * Fallback locale that doesn't provide any translations.
         */
        @JvmStatic
        val Fallback = I18n(Locale.ROOT, PoData(emptyMap(), EmptyRule))
    }
}