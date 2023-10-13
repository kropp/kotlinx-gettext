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
import kotlin.test.assertNull

class PoDataTest {
    @Test
    fun test() {
        val po = PoData.read(resource("en.po"))

        assertEquals("", po["Update available"])
        assertEquals("{0,number,integer} updates available", po["Update available", 0])
        assertEquals("Update available", po["Update available", 1])
        assertEquals("{0,number,integer} updates available", po["Update available", 2])

        assertEquals("{0} doesn''t exist", po["{0} doesn't exist"])
        assertEquals(null, po["{0} doesn't exist", 1])

        assertEquals(null, po["nonexistent key"])
    }

    @Test
    fun multilineKey() {
        val po = PoData.read(resource("de.po"))

        assertEquals("Sehr sehr lange\n\nBeschreibung", po["Very very long\n\ndescription"])
    }

    @Test
    fun quotedStrings() {
        val po = PoData.read(resource("quoted.po"))

        assertEquals("Hallo \"Welt\"!", po["Hello \"World\"!"])
    }

    @Test
    fun excessWhitespace() {
        val po = PoData.read(resource("whitespace.po"))

        assertEquals("Hallo Welt!", po["Hello World!"])
    }

    @Test
    fun ignoreEmptyString() {
        val po = PoData.read(resource("empty.po"))

        assertNull(po["empty"])
    }

    @Test
    fun ignoreEmptyPlural() {
        val po = PoData.read(resource("empty-plural.po"))

        assertNull(po["Update available", 1])
        assertNull(po["Update available", 2])
    }

    @Test
    fun someEmptyPlural() {
        val po = PoData.read(resource("some-plural.po"))

        assertEquals("Aktualisierung verfügbar", po["Update available", 1])
        assertNull(po["Update available", 2])
    }
}