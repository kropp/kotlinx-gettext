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

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@AutoService(CommandLineProcessor::class)
class GettextCommandLineProcessor : CommandLineProcessor {
    companion object {
        private const val OPTION_POT_FILE = "potFile"
        private const val OPTION_BASE_DIR = "baseDir"
        private const val OPTION_KEYWORD = "k"

        val ARG_BASE_DIR = CompilerConfigurationKey<String>(OPTION_POT_FILE)
        val ARG_POT_FILE = CompilerConfigurationKey<String>(OPTION_BASE_DIR)
        val ARG_KEYWORDS = CompilerConfigurationKey<List<String>>(OPTION_KEYWORD)
    }

    override val pluginId: String = KOTLIN_PLUGIN_ID

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(
            optionName = OPTION_BASE_DIR,
            valueDescription = "directory",
            description = "base path to files",
            required = false,
        ),
        CliOption(
            optionName = OPTION_POT_FILE,
            valueDescription = "file",
            description = ".pot file to extract messages to",
            required = true,
        ),
        CliOption(
            optionName = OPTION_KEYWORD,
            valueDescription = "keyword",
            description = "method's name to look for, in xgettext format",
            allowMultipleOccurrences = true,
            required = false
        )
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {
        return when (option.optionName) {
            OPTION_BASE_DIR -> configuration.put(ARG_BASE_DIR, value)
            OPTION_POT_FILE -> configuration.put(ARG_POT_FILE, value)
            OPTION_KEYWORD -> configuration.add(ARG_KEYWORDS, value)
            else -> throw IllegalArgumentException("Unexpected config option ${option.optionName}")
        }
    }
}