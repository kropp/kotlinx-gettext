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
import java.util.*
import kotlin.test.assertEquals

class I18nTest {
    @Test
    fun test() {
        val i18n = I18n.load(Locale("ru"), Thread.currentThread().contextClassLoader.getResourceAsStream("ru.po")!!)

        assertEquals("Установлена", i18n.trc("feminine", "Installed"))
        assertEquals("Установлен", i18n.trc("masculine", "Installed"))
    }
}