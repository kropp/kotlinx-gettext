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
        val po = PoFile.read(s.byteInputStream())

        assertEquals("", po.header.text)
        assertEquals(1, po.header.cases.size)
        assertEquals(1, po.entries.size)
        assertEquals("message-id", po.entries[0].text)
        assertEquals(1, po.entries[0].cases.size)
        assertEquals("", po.entries[0].cases[0])
    }

    @Test
    fun `read quoted entry`() {
        val s = "$DEFAULT_POT_HEADER\n\nmsgid \"message-id\"\nmsgstr \"Hello \\\"World\\\"!\"\n"
        val po = PoFile.read(s.byteInputStream())

        assertEquals("", po.header.text)
        assertEquals(1, po.header.cases.size)
        assertEquals(1, po.entries.size)
        assertEquals("message-id", po.entries[0].text)
        assertEquals(1, po.entries[0].cases.size)
        assertEquals("Hello \"World\"!", po.entries[0].cases[0])
    }

    @Test
    fun `write quoted entry`() {
        val out = ByteArrayOutputStream()
        PoFile(listOf(PoEntry(emptyList(), emptyList(), emptyList(), null, emptyList(), null, "message-id", null, listOf("Hello \"World\"!")))).write(out)
        assertEquals("$DEFAULT_POT_HEADER\n\nmsgid \"message-id\"\nmsgstr \"Hello \\\"World\\\"!\"\n", out.toString())
    }

    @Test
    fun `read entry with newline`() {
        val s = "$DEFAULT_POT_HEADER\n\nmsgid \"message-id\"\nmsgstr \"Hello\\n\\\"World\\\"!\"\n"
        val po = PoFile.read(s.byteInputStream())

        assertEquals("", po.header.text)
        assertEquals(1, po.header.cases.size)
        assertEquals(1, po.entries.size)
        assertEquals("message-id", po.entries[0].text)
        assertEquals(1, po.entries[0].cases.size)
        assertEquals("Hello\n\"World\"!", po.entries[0].cases[0])
    }

    @Test
    fun `write entry with newline`() {
        val out = ByteArrayOutputStream()
        PoFile(listOf(PoEntry(emptyList(), emptyList(), emptyList(), null, emptyList(), null, "message-id", null, listOf("Hello\n\"World\"!")))).write(out)
        assertEquals("$DEFAULT_POT_HEADER\n\nmsgid \"message-id\"\nmsgstr \"Hello\\n\\\"World\\\"!\"\n", out.toString())
    }

    private val empty = PoEntry(emptyList(), emptyList(), emptyList(), null, emptyList(), null, "", null, emptyList())
}