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

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory

open class GettextGradleExtension(objects: ObjectFactory, projectLayout: ProjectLayout) {
    /**
     * Allows enabling and disabling Gettext Kotlin compiler extension.
     * It is **off** by default.
     */
    val enabled = objects.property(Boolean::class.java).convention(false)

    /**
     * Base directory for reference paths in generated .pot file.
     * Doesn't affect the files to process.
     * Default is project directory.
     */
    val baseDir: DirectoryProperty = objects.directoryProperty().convention(projectLayout.projectDirectory)

    /**
     * Path to generated .pot file.
     * Default is `build/gettext/i18n.pot`
     */
    val potFile: RegularFileProperty = objects.fileProperty().convention(projectLayout.buildDirectory.file("gettext/i18n.pot"))

    /**
     * Forces overwriting the .pot file. When disabled, it tries to merge .pot with existing entries.
     * It is **off** by default.
     */
    val overwrite = objects.property(Boolean::class.java).convention(false)

    /**
     * Method names to search for. Uses gettext keywords format.
     * Default is `tr`.
     */
    val keywords = objects.listProperty(String::class.java).convention(listOf("tr"))
}