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

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import java.io.File

class GettextIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val keywords: List<String>,
    private val basePath: File,
    private val overwrite: Boolean,
    private val potFile: File,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val poEntries = mutableListOf<PoEntry>()

        for (file in moduleFragment.files) {
            val f = File(file.fileEntry.name)
            val relativePath =
                try {
                    f.relativeTo(basePath).path
                } catch (_: Throwable) {
                    f.name
                }
            GettextExtractor(
                messageCollector,
                poEntries,
                keywords.map { KeywordSpec(it) },
                relativePath,
                file.fileEntry
            )
                .visitFile(file)
        }

        messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Collected ${poEntries.size} entries")

        val poFile = if (overwrite) {
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Overwriting $potFile")
            PoFile.fromUnmerged(poEntries)
        } else if (!potFile.exists()) {
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Creating $potFile")
            PoFile.fromUnmerged(poEntries)
        } else {
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Updating $potFile")
            val original = potFile.inputStream().use { PoFile.read(it) }
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Original $potFile: $original")
            val updated = original.update(poEntries)
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Updated $potFile: $updated")
            updated
        }
        messageCollector.report(
            CompilerMessageSeverity.INFO,
            "[gettext] Writing ${poFile.entries.size} entries to $potFile"
        )
        potFile.outputStream().use {
            poFile.write(it)
        }
    }
}