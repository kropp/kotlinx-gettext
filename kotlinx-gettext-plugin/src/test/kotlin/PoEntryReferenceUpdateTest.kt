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

class PoEntryReferenceUpdateTest {
    @Test
    fun simple() {
        val po = PoFile(listOf(
            entry("id1", "src/main/kotlin/File.kt:123")
        )).update(listOf(
            entry("id1", "src/main/kotlin/File.kt:125")
        ))

        assertEquals(1, po.entries.size)
        assertEquals(1, po.entries[0].references.size)
        assertEquals("src/main/kotlin/File.kt:125", po.entries[0].references[0])
    }

    @Test
    fun multiple() {
        val po = PoFile(listOf(
            entry("id1", "src/main/kotlin/File.kt:123", "src/main/kotlin/Other.kt:87"),
            entry("id2", "src/main/kotlin/File.kt:45"),
        )).update(listOf(
            entry("id1", "src/main/kotlin/File.kt:125"),
            entry("id3"),
        ))

        assertEquals(3, po.entries.size)
        assertEquals(2, po.entries[0].references.size)
        assertEquals("src/main/kotlin/Other.kt:87", po.entries[0].references[0])
        assertEquals("src/main/kotlin/File.kt:125", po.entries[0].references[1])
        assertEquals(1, po.entries[1].references.size)
        assertEquals("src/main/kotlin/File.kt:45", po.entries[1].references[0])
        assertEquals(0, po.entries[2].references.size)
    }

    private fun entry(id: String, vararg references: String) = PoEntry(emptyList(), emptyList(), references.toList(), null, emptyList(), null, id, null, emptyList())
}