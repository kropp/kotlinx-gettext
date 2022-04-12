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

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

const val KOTLIN_PLUGIN_ID = "name.kropp.kotlinx-gettext"

@AutoService(ComponentRegistrar::class)
class GettextComponentRegistrar(
    private val defaultPotFile: String,
    private val defaultKeywords: List<String>,
) : ComponentRegistrar {

    @Suppress("unused") // Used by service loader
    constructor() : this(
        defaultPotFile = "i18n.pot",
        defaultKeywords = listOf("tr"),
    )

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        val file = File(configuration.get(GettextCommandLineProcessor.ARG_POT_FILE, defaultPotFile))
        runCatching { file.parentFile.mkdirs() }

        messageCollector.report(CompilerMessageSeverity.INFO, "Extracting strings to pot file $file")

        val overwrite = configuration.getBoolean(GettextCommandLineProcessor.ARG_OVERWRITE)
        val keywords = configuration.getList(GettextCommandLineProcessor.ARG_KEYWORDS).ifEmpty { defaultKeywords }
        val basePath = configuration.get(GettextCommandLineProcessor.ARG_BASE_DIR)?.let(::File) ?: File("")

        val extension = GettextIrGenerationExtension(messageCollector, keywords, basePath, overwrite, file)

        IrGenerationExtension.registerExtension(project, extension)
    }
}