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

 import okio.source
import java.io.InputStream
import java.text.MessageFormat

/**
 * Translate [MessageFormat]-compatible formatted [text] with provided [args].
 */
public fun I18n.tr(text: String, vararg args: Any?): String =
    MessageFormat.format(tr(text), *args)

/**
 * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
 * with provided [args], chosen based on provided number [n].
 */
public fun I18n.trn(text: String, plural: String, n: Int, vararg args: Any?): String =
    MessageFormat.format(trn(text, plural, n), *args)

/**
 * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
 * with provided [args], chosen based on provided number [n].
 */
public fun I18n.trn(text: String, plural: String, n: Long, vararg args: Any?): String =
    trn(text, plural, n.toInt(), *args)

/**
 * Translate [MessageFormat]-compatible formatted [text] with provided [args] in a given [context].
 */
public fun I18n.trc(context: String, text: String, vararg args: Any?): String =
    MessageFormat.format(trc(context, text), *args)

/**
 * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
 * with provided [args], chosen based on provided number [n] in a given [context].
 */
public fun I18n.trnc(context: String, text: String, plural: String, n: Int, vararg args: Any?): String =
    MessageFormat.format(trnc(context, text, plural, n), *args)

/**
 * Translate [MessageFormat]-compatible formatted [text] with different [plural] form
 * with provided [args], chosen based on provided number [n] in a given [context].
 */
public fun I18n.trnc(context: String, text: String, plural: String, n: Long, vararg args: Any?): String =
    trnc(context, text, plural, n.toInt(), *args)

/**
 * Load translations for given [locale] from given input stream.
 */
public fun Gettext.Companion.load(locale: Locale, s: InputStream): Gettext =
    load(locale, s.source())
