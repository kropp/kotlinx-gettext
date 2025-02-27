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

package name.kropp.kotlinx.gettext.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

const val KOTLIN_PLUGIN_ID = "name.kropp.kotlinx-gettext"
const val KOTLIN_PLUGIN_GROUP = "name.kropp.kotlinx-gettext"
const val KOTLIN_PLUGIN_NAME = "kotlinx-gettext-plugin"
const val KOTLIN_PLUGIN_VERSION = "0.7.0"

@Suppress("unused")
class KotlinxGettextGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
        val extension = target.extensions.create("gettext", GettextGradleExtension::class.java)
        target.tasks.register("gettext") {
            it.dependsOn(target.tasks.withType(KotlinCompilationTask::class.java))
        }
        if (target.gradle.startParameter.taskNames.any { it.endsWith("gettext") }) {
            extension.enabled.set(true)
        }
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return kotlinCompilation.target.project.extensions.getByType(GettextGradleExtension::class.java).enabled.get()
    }

    override fun getCompilerPluginId(): String = KOTLIN_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = KOTLIN_PLUGIN_GROUP,
        artifactId = KOTLIN_PLUGIN_NAME,
        version = KOTLIN_PLUGIN_VERSION
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(GettextGradleExtension::class.java)
        return project.provider {
            listOf(
                SubpluginOption(key = "baseDir", value = extension.baseDir.get().asFile.path),
                SubpluginOption(key = "potFile", value = extension.potFile.get().asFile.path),
                SubpluginOption(key = "overwrite", value = extension.overwrite.get().toString()),
                // ;COMMA; - magic separator used instead of comma, because comma is used by compiler
                // see GettextCommandLineProcessor.kt
            ) + extension.keywords.get().map { SubpluginOption(key = "keyword", value = it.replace(",", ";COMMA;")) }
        }
    }
}
