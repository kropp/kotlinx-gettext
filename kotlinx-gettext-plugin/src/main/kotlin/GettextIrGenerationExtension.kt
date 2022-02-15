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

package com.github.kropp.kotlinx.gettext

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import java.io.File

class GettextIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val keywords: List<String>,
    private val basePath: File,
    private val potFile: File,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val messages = mutableListOf<PoEntry>()

        for (file in moduleFragment.files) {
            val f = File(file.fileEntry.name)
            val relativePath =
                try {
                    f.relativeTo(basePath).path
                } catch (_: Throwable) {
                    f.name
                }
            val extractor = GettextExtractor(messageCollector, keywords.map { KeywordSpec(it) }, relativePath, file.fileEntry)
            extractor.visitFile(file)
            messages += extractor.poEntries
        }

        potFile.outputStream().use {
            PoFile.fromUnmerged(messages).write(it)
        }
    }
}