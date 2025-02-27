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

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test
import java.io.File
import kotlin.test.assertEquals

class SmokeTest {
    @Test
    fun smoke() {
        val result = compile(
            SourceFile.kotlin(
                "main.kt", """
fun main() {
  println("Hello, World!")
}
"""
            )
        )
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun tr() {
        val result = compile(
            SourceFile.kotlin(
                "main.kt", """
fun main() {
  tr("Hello, World!")
}
fun tr(text: String) {}
"""
            )
        )
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun stringLiteral() {
        val result = compile(
            SourceFile.kotlin(
                "main.kt", """
fun main() {
  tr(""" + "\"\"\"Hello, World!\"\"\"" + """)
}
fun tr(text: String) {}
"""
            )
        )
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        val potFile = File(result.outputDirectory.parentFile, "i18n.pot").readText()
        assertEquals("$DEFAULT_POT_HEADER\n\n#: main.kt:2\nmsgid \"Hello, World!\"\nmsgstr \"\"\n", potFile)
    }

    @Test
    fun `custom keyword`() {
        val result = compile(
            SourceFile.kotlin(
                "main.kt", """
fun main() {
  custom("Hello, World!")
}
fun custom(text: String) {}
"""
            ),
            defaultKeywords = listOf("custom")
        )
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun context() {
        val result = compile(
            SourceFile.kotlin(
                "main.kt", """
fun main() {
  trc("context", "Hello, World!")
}
fun trc(ctx: String, text: String) {}
"""
            ),
            defaultKeywords = listOf("trc:1c,2")
        )
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun stringWithQuotes() {
        val result = compile(SourceFile.kotlin("main.kt", """
    fun main() {
      tr("Hello, \"World\"!")
    }
    fun tr(text: String) {}
            """.trimIndent()
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        val potFile = File(result.outputDirectory.parentFile, "i18n.pot").readText()
        assertEquals("$DEFAULT_POT_HEADER\n\n#: main.kt:2\nmsgid \"Hello, \\\"World\\\"!\"\nmsgstr \"\"\n", potFile)
    }

    @Test
    fun stringWithNewline() {
        val result = compile(SourceFile.kotlin("main.kt", """
    fun main() {
      tr("Hello,\n\"World\"!")
    }
    fun tr(text: String) {}
            """.trimIndent()
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        val potFile = File(result.outputDirectory.parentFile, "i18n.pot").readText()
        assertEquals("$DEFAULT_POT_HEADER\n\n#: main.kt:2\nmsgid \"Hello,\\n\\\"World\\\"!\"\nmsgstr \"\"\n", potFile)
    }

    @OptIn(ExperimentalCompilerApi::class)
    private fun compile(vararg sourceFile: SourceFile, defaultKeywords: List<String> = listOf("tr")): KotlinCompilation.Result {
        return KotlinCompilation().apply {
            sources = sourceFile.toList()
            useIR = true
            compilerPluginRegistrars = listOf(GettextCompilerPluginRegistrar(File(workingDir, "i18n.pot").absolutePath, defaultKeywords))
            inheritClassPath = true
        }.compile()
    }
}