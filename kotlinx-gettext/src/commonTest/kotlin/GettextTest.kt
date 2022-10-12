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

import kotlin.test.Test
import kotlin.test.assertEquals

class GettextTest {
    @Test
    fun context() {
        val gettext = Gettext.load(Locale("ru"), resource("ru.po"))

        assertEquals("Установлена", gettext.trc("feminine", "Installed"))
        assertEquals("Установлен", gettext.trc("masculine", "Installed"))
    }

    @Test
    fun formatting() {
        val gettext = Gettext.load(Locale("ru"), resource("format.po"))

        assertEquals("Hallo Gettext!", gettext.tr("Hello {{name}}!", "name" to "Gettext"))
    }

    @Test
    fun empty() {
        val gettext = Gettext.load(Locale("de"), resource("empty.po"))

        assertEquals("empty", gettext.tr("empty"))
    }
}