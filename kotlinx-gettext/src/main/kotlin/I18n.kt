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

interface I18n {
    /**
     * Current locale.
     */
    val locale: Locale

    /**
     * Translate [text].
     */
    fun tr(text: String): String

    /**
     * Translate [MessageFormat]-compatible formatted [text] with provided [args].
     */
    fun tr(text: String, vararg args: Any?): String

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Int): String

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Long): String

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Int, vararg args: Any?): String

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n].
     */
    fun trn(text: String, plural: String, n: Long, vararg args: Any?): String

    /**
     * Translate [text] in a given [context].
     */
    fun trc(context: String, text: String): String

    /**
     * Translate [MessageFormat]-compatible formatted [text] with provided [args] in a given [context].
     */
    fun trc(context: String, text: String, vararg args: Any?): String

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Int): String

    /**
     * Translate [text] with different [plural] form, chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Long): String

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Int, vararg args: Any?): String

    /**
     * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
     * with provided [args], chosen based on provided number [n] in a given [context].
     */
    fun trnc(context: String, text: String, plural: String, n: Long, vararg args: Any?): String

    /**
     * Mark [text] for translation, but do not translate it.
     */
    fun marktr(text: String): String
}