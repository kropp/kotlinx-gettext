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

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class PoTest {
    @Test
    fun `write empty`() {
        val out = ByteArrayOutputStream()
        PoFile(emptyList(), empty).write(out)
        assertEquals("msgid \"\"\nmsgstr \"\"\n\n", out.toString())
    }

    @Test
    fun `write default`() {
        val out = ByteArrayOutputStream()
        PoFile(emptyList()).write(out)
        assertEquals("$DEFAULT_POT_HEADER\n", out.toString())
    }

    @Test
    fun `write single entry`() {
        val out = ByteArrayOutputStream()
        PoFile(listOf(PoEntry(emptyList(), emptyList(), emptyList(), null, emptyList(), null, "message-id", null, emptyList()))).write(out)
        assertEquals("$DEFAULT_POT_HEADER\n\nmsgid \"message-id\"\nmsgstr \"\"\n", out.toString())
    }

    @Test
    fun `read simple entry`() {
        val s = "$DEFAULT_POT_HEADER\n\nmsgid \"message-id\"\nmsgstr \"\"\n"
        println(s)
        val po = PoFile.read(s.byteInputStream())

        assertEquals("", po.header.text)
        assertEquals(1, po.header.cases.size)
        assertEquals(DEFAULT_POT_HEADER.cases[0], po.header.cases[0])
        assertEquals(1, po.entries.size)
        assertEquals("message-id", po.entries[0].text)
        assertEquals(1, po.entries[0].cases.size)
        assertEquals("", po.entries[0].cases[0])
    }

    private val empty = PoEntry(emptyList(), emptyList(), emptyList(), null, emptyList(), null, "", null, emptyList())
}